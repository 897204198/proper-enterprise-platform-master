package com.proper.enterprise.platform.websocket.client;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * Stomp异常处理器
 */
public interface StompExceptionHandler {

    /**
     * 处理异常
     *
     * @param session   session
     * @param command   命令
     * @param headers   请求头
     * @param payload   内容
     * @param exception 异常
     */
    void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception);

    /**
     * 处理连接失败
     *
     * @param session   session
     * @param exception 异常
     */
    void handleTransportError(StompSession session, Throwable exception);

}
