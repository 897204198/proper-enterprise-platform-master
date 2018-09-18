package com.proper.enterprise.platform.notice.server.sdk.enums;

/**
 * 消息状态
 */
public enum NoticeStatus {
    /**
     * 发送成功
     */
    SUCCESS,
    /**
     * 发送中
     */
    PENDING,
    /**
     * 等待重试
     */
    RETRY,
    /**
     * 发送失败
     */
    FAIL
}
