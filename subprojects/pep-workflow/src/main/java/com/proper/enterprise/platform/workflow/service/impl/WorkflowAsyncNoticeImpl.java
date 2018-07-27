package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.workflow.api.WorkflowAsyncNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WorkflowAsyncNoticeImpl implements WorkflowAsyncNotice {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowAsyncNoticeImpl.class);


    private JavaMailSender mailSender;

    @Autowired
    WorkflowAsyncNoticeImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void notice(SimpleMailMessage... message) {
        try {
            mailSender.send(message);
            LOGGER.info("WorkflowAsyncNotice msg:{}", JSONUtil.toJSON(message));
        } catch (Exception e) {
            LOGGER.error("workflowAsyncNotice error", e);
        }
    }
}
