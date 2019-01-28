package com.proper.enterprise.platform.websocket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class WebSocketClientEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientEndpoint.class);

    private Session userSession;
    private MessageHandler messageHandler;

    WebSocketClientEndpoint(URI endpointURI, MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            LOGGER.error("Error occurs when connecting to ws server {}", endpointURI, e);
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        LOGGER.info("Opening websocket connection {}", userSession.getId());
        this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        LOGGER.info("Closing websocket connection {} for reason {}", userSession.getId(), reason);
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        } else {
            LOGGER.warn("Received message {}, but no message handler, "
                        + "use WebSocketClientEndpoint.setMessageHandler to set one before use!", message);
        }
    }

    /**
     * Send a message.
     *
     * @param message message
     */
    void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    /**
     * Message handler.
     */
    public interface MessageHandler {

        void handleMessage(String message);

    }

}
