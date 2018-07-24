package com.proper.enterprise.platform.workflow.api;

import org.springframework.mail.SimpleMailMessage;

public interface WorkflowAsyncNotice {
    /**
     * 异步通知
     *
     * @param message 消息内容
     */
    void notice(SimpleMailMessage message);
}
