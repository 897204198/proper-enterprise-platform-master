package com.proper.enterprise.platform.websocket.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class WebSocketClient {

    private static Map<String, WebSocketClient> clientRegistry = new HashMap<>(16);

    /**
     * Client 唯一标识，可用来作为单播消息的用户标识
     */
    private String clientId;

    /**
     * WebSocket endpoint URL
     */
    private String url;

    private WebSocketClientEndpoint endpoint;

    private WebSocketClient(String clientId, String url) {
        this.clientId = clientId;
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format("WebSocketClient: [clientId: %s, url: %s]", clientId, url);
    }

    /**
     * 连接至 url，并给客户端标识为 clientId
     *
     * @param  clientId 客户端标识（自定义）
     * @param  url      连接地址
     * @param  handler  消息处理器
     * @return 客户端实例
     * @throws URISyntaxException URI 语法异常
     */
    public static WebSocketClient connect(String clientId, String url, WebSocketClientEndpoint.EventHandler handler) throws URISyntaxException {
        WebSocketClient client = new WebSocketClient(clientId, url);
        client.endpoint = new WebSocketClientEndpoint(new URI(url), handler);
        clientRegistry.put(clientId, client);
        return client;
    }

    /**
     * 根据客户端 ID 获得客户端实例
     *
     * @param  clientId ID
     * @return 客户端实例
     */
    public static WebSocketClient getInstance(String clientId) {
        return clientRegistry.get(clientId);
    }

    /**
     * 发送消息
     *
     * @param message 消息内容
     */
    public void send(String message) {
        clientRegistry.get(clientId).endpoint.sendMessage(message);
    }

    /**
     * 断开客户端连接
     */
    public void disconnect() {
        WebSocketClient client = clientRegistry.get(clientId);
        client.endpoint.disconnect();
        clientRegistry.remove(clientId);
    }

}
