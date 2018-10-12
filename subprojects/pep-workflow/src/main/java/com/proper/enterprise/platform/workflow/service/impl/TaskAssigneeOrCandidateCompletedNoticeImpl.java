package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.service.NoticeSender;
import com.proper.enterprise.platform.workflow.api.AbstractWorkFlowNoticeSupport;
import com.proper.enterprise.platform.workflow.api.TaskAssigneeOrCandidateCompletedNotice;
import com.proper.enterprise.platform.workflow.util.VariableUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Service("taskCompletedNotice")
public class TaskAssigneeOrCandidateCompletedNoticeImpl extends AbstractWorkFlowNoticeSupport implements TaskAssigneeOrCandidateCompletedNotice {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskAssigneeOrCandidateCompletedNoticeImpl.class);

    public static final String TASK_COMPLETED_NOTICE_CODE_KEY = "taskCompletedNoticeCode";

    private NoticeSender noticeSender;

    @Autowired
    TaskAssigneeOrCandidateCompletedNoticeImpl(NoticeSender noticeSender) {
        this.noticeSender = noticeSender;
    }

    @Override
    public void notice(TaskEntity task) {
        try {
            Set<String> userIds = queryUserIds(task);
            if (StringUtil.isNotEmpty(task.getAssignee())) {
                userIds.remove(task.getAssignee());
            }
            if (CollectionUtil.isEmpty(userIds)) {
                return;
            }
            Map<String, Object> templateParams = new HashMap<>(5);
            templateParams.putAll(VariableUtil.convertVariableToMsgParam(task.getVariables()));
            templateParams.put("taskName", task.getName());
            Map<String, Object> custom = new HashMap<>(0);
            custom.put("gdpr_mpage", "examList");
            custom.put("title", task.getName());
            String noticeCode = (String) task.getVariable(TASK_COMPLETED_NOTICE_CODE_KEY);
            noticeSender.sendNotice(userIds, StringUtil.isEmpty(noticeCode) ? "taskCompletedNotice" : noticeCode,
                templateParams, custom);
        } catch (Exception e) {
            LOGGER.error("taskCompletedNoticeError", e);
        }
    }
}
