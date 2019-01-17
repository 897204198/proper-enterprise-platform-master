package com.proper.enterprise.platform.websocket.client;

import org.springframework.messaging.simp.stomp.StompHeaders;

import java.util.concurrent.CountDownLatch;

public class TestBaseStompSessionAdapter implements StompMessageFrameHandler {

    private CountDownLatch latch;

    public TestBaseStompSessionAdapter(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        if (payload != null) {
            latch.countDown();
        }
    }
}
