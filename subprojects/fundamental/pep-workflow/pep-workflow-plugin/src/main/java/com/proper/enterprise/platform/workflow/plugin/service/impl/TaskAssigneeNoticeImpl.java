package com.proper.enterprise.platform.workflow.plugin.service.impl;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.api.AbstractWorkFlowNoticeSupport;
import com.proper.enterprise.platform.workflow.plugin.service.TaskAssigneeOrCandidateNotice;
import com.proper.enterprise.platform.workflow.plugin.service.WorkflowAsyncNotice;
import com.proper.enterprise.platform.workflow.util.VariableUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("taskAssigneeNotice")
@Deprecated
/**
 * // TODO: 2018/8/17 兼容旧流程十月删除 删除前需查看旧流程是否还存在
 */
public class TaskAssigneeNoticeImpl extends AbstractWorkFlowNoticeSupport implements TaskAssigneeOrCandidateNotice {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskAssigneeOrCandidateNoticeImpl.class);

    public static final String TASK_ASSIGNEE_NOTICE_CODE_KEY = "taskAssigneeNoticeCode";

    private WorkflowAsyncNotice noticeSender;

    @Autowired
    TaskAssigneeNoticeImpl(WorkflowAsyncNotice noticeSender) {
        this.noticeSender = noticeSender;
    }

    @Override
    public void notice(TaskEntity task) {
        try {
            Set<String> userIds = queryUserIds(task);
            if (CollectionUtil.isEmpty(userIds)) {
                return;
            }
            Map<String, Object> templateParams = new HashMap<>(5);
            templateParams.putAll(VariableUtil.convertVariableToMsgParam(task.getVariables()));
            templateParams.put("taskName", task.getName());
            templateParams.put("pageurl", buildTaskUrl(task) + "&from=email");
            Map<String, Object> custom = new HashMap<>(0);
            custom.put("gdpr_mpage", "examList");
            custom.put("url", buildTaskUrl(task) + "&from=app");
            custom.put("title", task.getName());
            String noticeCode = (String) task.getVariable(TASK_ASSIGNEE_NOTICE_CODE_KEY);
            noticeSender.sendAsyncNotice(StringUtil.isEmpty(noticeCode) ? "TaskAssignee" : noticeCode,
                custom, userIds, templateParams);
        } catch (Exception e) {
            LOGGER.error("taskAssigneeNoticeError", e);
        }
    }
}
