package com.proper.enterprise.platform.websocket.client;

/**
 * stomp订阅对象
 */
public class StompSubscribe {

    public StompSubscribe(String subscribe, StompMessageFrameHandler stompMessageFrameHandler) {
        this.subscribe = subscribe;
        this.stompMessageFrameHandler = stompMessageFrameHandler;
    }

    /**
     * 订阅地址
     */
    private String subscribe;

    /**
     * 消息处理器
     */
    private StompMessageFrameHandler stompMessageFrameHandler;

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public StompMessageFrameHandler getStompMessageFrameHandler() {
        return stompMessageFrameHandler;
    }

    public void setStompMessageFrameHandler(StompMessageFrameHandler stompMessageFrameHandler) {
        this.stompMessageFrameHandler = stompMessageFrameHandler;
    }
}
