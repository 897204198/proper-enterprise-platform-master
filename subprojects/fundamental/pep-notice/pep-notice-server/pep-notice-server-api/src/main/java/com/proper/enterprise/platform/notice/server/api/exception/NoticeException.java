package com.proper.enterprise.platform.notice.server.api.exception;

/**
 * 消息异常
 */
public class NoticeException extends Exception {

    public NoticeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoticeException(String message) {
        super(message);
    }
}
