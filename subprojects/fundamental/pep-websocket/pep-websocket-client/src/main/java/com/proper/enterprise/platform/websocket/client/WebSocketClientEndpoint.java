package com.proper.enterprise.platform.websocket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

/**
 * See also {@link javax.websocket.Endpoint}
 */
@ClientEndpoint
public class WebSocketClientEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientEndpoint.class);

    private Session userSession;
    private EventHandler eventHandler;

    WebSocketClientEndpoint(URI endpointURI, EventHandler msgHandler) {
        this.eventHandler = msgHandler;
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
        this.eventHandler.onClose();
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.eventHandler != null) {
            this.eventHandler.onMessage(message);
        } else {
            LOGGER.warn("Received message {}, but no message handler, "
                        + "use WebSocketClientEndpoint.setMessageHandler to set one before use!", message);
        }
    }

    /**
     * Event that is triggered when a protocol error occurs.
     *
     * @param throwable The exception.
     */
    @OnError
    public void onError(Session userSession, Throwable throwable) {
        LOGGER.error("Error occurs on {}", userSession.getId(), throwable);
        this.userSession = null;
        this.eventHandler.onError();
    }

    /**
     * Send a message.
     *
     * @param message message
     */
    void sendMessage(String message) {
        Assert.notNull(userSession, "Could NOT get session, connection maybe already closed or error!");
        userSession.getAsyncRemote().sendText(message);
    }

    void disconnect() {
        Assert.notNull(userSession, "Could NOT get session, connection maybe already closed or error!");
        if (userSession.isOpen()) {
            try {
                userSession.close();
            } catch (IOException e) {
                LOGGER.error("Error occurs when closing connection {}", userSession.getId(), e);
            }
        }
    }

    /**
     * Event handler.
     */
    public interface EventHandler {

        /**
         * Handle received message
         *
         * @param message message
         */
        void onMessage(String message);

        /**
         * Handle error event, default to do nothing
         */
        default void onError() { }

        /**
         * Handle close event, default to do nothing
         */
        default void onClose() { }

    }

}
