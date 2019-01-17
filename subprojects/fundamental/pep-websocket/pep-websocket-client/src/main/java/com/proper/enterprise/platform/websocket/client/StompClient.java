package com.proper.enterprise.platform.websocket.client;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StompClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(StompClient.class);

    public StompClient(String url, StompSubscribe... subscribes) {
        this.url = url;
        this.subscribes = subscribes;
    }

    public StompClient(String url, StompExceptionHandler stompExceptionHandler, StompSubscribe... subscribes) {
        this.url = url;
        this.subscribes = subscribes;
        this.stompExceptionHandler = stompExceptionHandler;
    }

    public StompClient(String url, String stompUser, StompExceptionHandler stompExceptionHandler, StompSubscribe... subscribes) {
        this.url = url;
        this.stompUser = stompUser;
        this.subscribes = subscribes;
        this.stompExceptionHandler = stompExceptionHandler;
    }

    public StompClient(String url, String stompUser,
                       StompExceptionHandler stompExceptionHandler,
                       Type payloadType, StompSubscribe... subscribes) {
        this.url = url;
        this.stompUser = stompUser;
        this.subscribes = subscribes;
        this.stompExceptionHandler = stompExceptionHandler;
        this.payloadType = payloadType;

    }

    public StompClient(String url, String stompUser,
                       StompExceptionHandler stompExceptionHandler,
                       Type payloadType, AbstractMessageConverter messageConverter,
                       boolean needReconnect, StompSubscribe... subscribes) {
        this.url = url;
        this.stompUser = stompUser;
        this.subscribes = subscribes;
        this.stompExceptionHandler = stompExceptionHandler;
        this.payloadType = payloadType;
        this.messageConverter = messageConverter;
        this.needReconnect = needReconnect;
    }

    /**
     * sessionId
     */
    private String sessionId;

    /**
     * url
     */
    private String url;

    /**
     * 订阅用户标识
     */
    private String stompUser;

    /**
     * 订阅集合
     */
    private StompSubscribe[] subscribes;

    /**
     * 异常处理器
     */
    private StompExceptionHandler stompExceptionHandler;

    /**
     * 消息转换 默认string
     */
    private AbstractMessageConverter messageConverter = new StringMessageConverter();

    /**
     * 消息类型 默认string
     */
    private Type payloadType = String.class;

    /**
     * 注册的client
     */
    private WebSocketStompClient webSocketStompClient;

    /**
     * 是否需要重连 默认true
     */
    private boolean needReconnect = true;

    /**
     * 连接后的session
     */
    private StompSession stompSession;

    /**
     * 正在连接标识
     */
    private boolean connecting;

    public StompClient connect() {
        webSocketStompClient = createClient(messageConverter);
        StompHeaders stompHeaders = new StompHeaders();
        if (StringUtil.isNotEmpty(stompUser)) {
            stompHeaders.add(PEPConstants.STOMP_USER_HEADER, stompUser);
        }
        connecting = true;
        webSocketStompClient.connect(url, (WebSocketHttpHeaders) null, stompHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                stompSession = session;
                connecting = false;
                for (StompSubscribe subscribe : subscribes) {
                    session.subscribe(subscribe.getSubscribe(), new StompFrameHandler() {
                        @Override
                        public Type getPayloadType(StompHeaders headers) {
                            return payloadType;
                        }

                        @Override
                        public void handleFrame(StompHeaders headers, Object payload) {
                            subscribe.getStompMessageFrameHandler().handleFrame(headers, payload);
                        }
                    });
                }
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                connecting = false;
                if (null != stompExceptionHandler) {
                    stompExceptionHandler.handleException(session, command, headers, payload, exception);
                }
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                connecting = false;
                if (null != stompExceptionHandler) {
                    stompExceptionHandler.handleTransportError(session, exception);
                }
                if (needReconnect && !session.isConnected()) {
                    LOGGER.info("reConnect,sessionId:{}", sessionId);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        LOGGER.error("wait reConnect sleep error", e);
                    }
                    connect();
                }
            }
        });
        return this;
    }

    public void disConnect() {
        this.needReconnect = false;
        webSocketStompClient.stop();
    }


    private WebSocketStompClient createClient(MessageConverter msgConverter) {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        org.springframework.web.socket.client.WebSocketClient webSocketClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(msgConverter);
        return stompClient;
    }

    public StompSession getStompSession() throws InterruptedException {
        while (connecting) {
            Thread.sleep(10);
        }
        return stompSession;
    }

    public void setStompSession(StompSession stompSession) {
        this.stompSession = stompSession;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void addSubscribe(StompSubscribe stompSubscribe) throws InterruptedException {
        getStompSession().subscribe(stompSubscribe.getSubscribe(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return payloadType;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                stompSubscribe.getStompMessageFrameHandler().handleFrame(headers, payload);
            }
        });
    }
}
