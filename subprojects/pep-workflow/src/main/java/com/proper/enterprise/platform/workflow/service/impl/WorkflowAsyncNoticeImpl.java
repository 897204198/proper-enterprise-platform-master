package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.workflow.api.WorkflowAsyncNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WorkflowAsyncNoticeImpl implements WorkflowAsyncNotice {

    private JavaMailSender mailSender;

    @Autowired
    WorkflowAsyncNoticeImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void notice(SimpleMailMessage message) {
        mailSender.send(message);
    }
}
