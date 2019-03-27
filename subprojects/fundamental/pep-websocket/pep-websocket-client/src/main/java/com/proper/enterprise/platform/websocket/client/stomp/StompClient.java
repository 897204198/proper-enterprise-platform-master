package com.proper.enterprise.platform.websocket.client.stomp;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class StompClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(StompClient.class);

    /**
     * 使用 STOMP User 标识作为 key 的 STOMP 客户端 Map
     */
    private static Map<String, StompClient> clientRegistry = new HashMap<>(16);

    /**
     * Client 唯一标识，可用来作为单播消息的用户标识
     */
    private String clientId;

    /**
     * WebSocket endpoint URL
     */
    private String url;

    /**
     * 连接后的session
     */
    private StompSession stompSession;

    /**
     * 连接闭锁，用来等待连接成功
     */
    private CountDownLatch connectLatch = new CountDownLatch(1);

    /**
     * 断线重连间隔时间，单位：秒
     */
    private static final int[] RETRY_INTERVALS = {15, 15, 30, 180, 1800, 1800, 1800, 1800, 3600};

    /**
     * 断线重连重试闭锁
     */
    private CountDownLatch retryLatch = new CountDownLatch(RETRY_INTERVALS.length);

    private WebSocketStompClient webSocketStompClient;

    private Map<String, StompSubscription> subscriptions = new HashMap<>(16);

    private StompClient(String clientId, String url) {
        this.clientId = clientId;
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format("StompClient: [clientId: %s, url: %s]", clientId, url);
    }

    /**
     * 连接至 url，并给客户端标识为 clientId
     *
     * @param clientId 客户端标识
     * @param url      连接地址
     */
    public static void connect(String clientId, String url) {
        StompClient client = clientRegistry.containsKey(clientId) ? clientRegistry.get(clientId) : new StompClient(clientId, url);
        if (client.webSocketStompClient == null) {
            client.webSocketStompClient = createClient(new StringMessageConverter());
        }
        client.webSocketStompClient.connect(url, (WebSocketHttpHeaders) null, getStompHeaders(clientId), new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                LOGGER.info("Client {} connected to {}.", clientId, url);
                client.stompSession = session;
                client.connectLatch.countDown();
                // Reset retry count
                client.retryLatch = new CountDownLatch(RETRY_INTERVALS.length);
                // Re-subscribe
                for (Map.Entry<String, StompSubscription> entry : client.subscriptions.entrySet()) {
                    session.subscribe(entry.getKey(), entry.getValue().getHandler());
                }
                // Update client after connected
                clientRegistry.put(clientId, client);
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                LOGGER.error("Exception occurs in client {} with command {}!", clientId, command, exception);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                LOGGER.debug("Transport error in client {}!", clientId, exception);
                if (session.isConnected()) {
                    LOGGER.debug("Client {} is connected, no need to reconnect", clientId);
                    return;
                }
                if (client.retryLatch.getCount() > 0) {
                    int interval = RETRY_INTERVALS[RETRY_INTERVALS.length - (int) client.retryLatch.getCount()];
                    LOGGER.info("Wait {} seconds to reconnect client {} ...", interval, clientId);
                    try {
                        Thread.sleep(interval * 1000);
                    } catch (InterruptedException e) {
                        LOGGER.debug("Thread is interrupted during sleep {} seconds.", interval, e);
                    }
                    LOGGER.info("Reconnecting client {} ...", clientId);
                    client.retryLatch.countDown();
                    // Update client after retry latch count down
                    clientRegistry.put(clientId, client);
                    connect(clientId, url);
                } else {
                    LOGGER.error("GIVE UP retry to reconnect client {} after {} tries.",
                        clientId, RETRY_INTERVALS.length - client.retryLatch.getCount(), exception);
                }
            }
        });
        // First time to put client into registry
        clientRegistry.put(clientId, client);
    }

    private static WebSocketStompClient createClient(MessageConverter msgConverter) {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient webSocketClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(msgConverter);
        // Client needs to enable heartbeat explicitly to keep idle connection alive
        // See more info at https://www.rabbitmq.com/heartbeats.html
        // See heartbeat part of Spring doc as below:
        // https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#websocket-stomp-client
        TaskScheduler scheduler;
        try {
            scheduler = PEPApplicationContext.getBean("taskScheduler", TaskScheduler.class);
        } catch (Exception e) {
            LOGGER.warn("Could NOT get task scheduler from Spring, create a new task scheduler as fallback.");
            scheduler = new ThreadPoolTaskScheduler();
            ((ThreadPoolTaskScheduler) scheduler).initialize();
        }
        stompClient.setTaskScheduler(scheduler);
        return stompClient;
    }

    private static StompHeaders getStompHeaders(String clientId) {
        StompHeaders stompHeaders = new StompHeaders();
        if (StringUtil.isNotEmpty(clientId)) {
            // 使用这个自定义 Header 标识客户端，可用来给指定客户端发送消息
            stompHeaders.add(PEPConstants.STOMP_USER_HEADER, clientId);
        }
        return stompHeaders;
    }

    /**
     * 使某 Client 订阅某个主题
     *
     * @param  clientId 客户端 id
     * @param  topic    主题
     * @param  handler  处理器
     * @throws InterruptedException 获得 session 时可能会抛出
     */
    public static void subscribe(String clientId, String topic, StompFrameHandler handler) throws InterruptedException {
        StompClient client = getClient(clientId);
        StompSession.Subscription subscription = getSession(clientId).subscribe(topic, handler);
        client.subscriptions.put(topic, new StompSubscription(topic, handler, subscription));
        clientRegistry.put(clientId, client);
    }

    private static StompClient getClient(String clientId) {
        Assert.isTrue(clientRegistry.containsKey(clientId), String.format("Could NOT find client with id '%s', maybe not connected.", clientId));
        return clientRegistry.get(clientId);
    }

    private static StompSession getSession(String clientId) throws InterruptedException, RuntimeException {
        StompClient client = getClient(clientId);
        int timeout = RETRY_INTERVALS[0];
        if (client.connectLatch.await(timeout, TimeUnit.SECONDS)) {
            return client.stompSession;
        } else {
            throw new RuntimeException(String.format("Could NOT get connected session to %s after %d seconds!", client.url, timeout));
        }
    }

    /**
     * 使某 Client 取消订阅某个主题
     *
     * @param clientId 客户端 id
     * @param topic    主题
     */
    public static void unsubscribe(String clientId, String topic) {
        StompClient client = getClient(clientId);
        if (client.subscriptions.containsKey(topic)) {
            client.subscriptions.get(topic).getSubscription().unsubscribe();
        }
    }

    /**
     * 使用某 Client 向指定目的地发送信息
     *
     * @param clientId    客户端 id
     * @param destination 消息目的地
     * @param payload     消息内容
     * @throws InterruptedException 获得 session 时可能会抛出
     */
    public static void send(String clientId, String destination, Object payload) throws InterruptedException {
        getSession(clientId).send(destination, payload);
    }

}
