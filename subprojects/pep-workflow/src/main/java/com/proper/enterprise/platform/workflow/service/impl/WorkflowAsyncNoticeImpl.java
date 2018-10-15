package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.notice.client.NoticeSender;
import com.proper.enterprise.platform.workflow.service.WorkflowAsyncNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class WorkflowAsyncNoticeImpl implements WorkflowAsyncNotice {

    private NoticeSender noticeSender;

    @Autowired
    public WorkflowAsyncNoticeImpl(NoticeSender noticeSender) {
        this.noticeSender = noticeSender;
    }

    @Override
    @Async
    public void sendAsyncNotice(String code, Map<String, Object> custom, String userId, Map<String, Object> templateParams) {
        Set<String> userIds = new HashSet<>();
        userIds.add(userId);
        this.sendAsyncNotice(code, custom, userIds, templateParams);
    }

    @Override
    @Async
    public void sendAsyncNotice(String code, Map<String, Object> custom, Set<String> userIds, Map<String, Object> templateParams) {
        noticeSender.sendNotice(code, custom, userIds, templateParams);
    }
}
