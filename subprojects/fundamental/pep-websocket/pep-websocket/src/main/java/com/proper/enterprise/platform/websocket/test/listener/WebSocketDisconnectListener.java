package com.proper.enterprise.platform.websocket.test.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketDisconnectListener.class);

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        LOGGER.info("DISCONNECT:SESSIONID:{}", event.getSessionId());
    }
}
