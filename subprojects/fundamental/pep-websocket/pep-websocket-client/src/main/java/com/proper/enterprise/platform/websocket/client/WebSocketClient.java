package com.proper.enterprise.platform.websocket.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class WebSocketClient {

    /**
     * 使用 STOMP User 标识作为 key 的 STOMP 客户端 Map
     */
    private static Map<String, WebSocketClientEndpoint> clientRegistry = new HashMap<>(16);

    public static void connect(String clientId, String url, WebSocketClientEndpoint.MessageHandler handler) throws URISyntaxException {
        WebSocketClientEndpoint endpoint = new WebSocketClientEndpoint(new URI(url), handler);
        clientRegistry.put(clientId, endpoint);
    }

    public static void send(String clientId, String message) {
        clientRegistry.get(clientId).sendMessage(message);
    }

}
