package com.proper.enterprise.platform.websocket.client.stomp;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * STOMP 订阅对象
 * 用来组织订阅主题、handler，和订阅成功后的对象（StompSession.Subscription）
 */
public class StompSubscription {

    /**
     * 订阅主题
     */
    private String topic;

    /**
     * 消息处理器
     */
    private StompFrameHandler handler;

    /**
     * A handle to use to unsubscribe or to track a receipt.
     */
    private StompSession.Subscription subscription;

    public StompSubscription(String topic, StompFrameHandler handler, StompSession.Subscription subscription) {
        this.topic = topic;
        this.handler = handler;
        this.subscription = subscription;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public StompFrameHandler getHandler() {
        return handler;
    }

    public void setHandler(StompFrameHandler handler) {
        this.handler = handler;
    }

    public StompSession.Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(StompSession.Subscription subscription) {
        this.subscription = subscription;
    }

}
