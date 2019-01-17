package com.proper.enterprise.platform.websocket.client;

import org.springframework.messaging.simp.stomp.StompHeaders;

/**
 * Stomp消息处理接口
 */
public interface StompMessageFrameHandler {

    /**
     * 处理消息
     *
     * @param headers 请求头
     * @param payload 内容
     */
    void handleFrame(StompHeaders headers, Object payload);


}
