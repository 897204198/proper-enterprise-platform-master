package com.proper.enterprise.platform.websocket.client.stomp;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

/**
 * 文字类型消息的 Handler 接口
 */
public interface StompFrameStringHandler extends StompFrameHandler {

    /**
     * 固定返回 String 类型
     * @param  headers the headers of a message
     * @return String 类型
     */
    @Override
    default Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

}
