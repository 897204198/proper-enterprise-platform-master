package com.proper.enterprise.platform.workflow.api.notice;

import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.api.AbstractWorkFlowNoticeSupport;
import com.proper.enterprise.platform.workflow.api.TaskAssigneeOrCandidateCompletedNotice;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("mockTaskCompletedNotice")
public class TaskAssigneeOrCandidateCompletedNoticeImpl extends AbstractWorkFlowNoticeSupport implements TaskAssigneeOrCandidateCompletedNotice {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskAssigneeOrCandidateCompletedNoticeImpl.class);

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
            Authentication.setCurrentUserId(task.getVariable("initiatorName")
                + " start "
                + task.getVariable("processDefinitionName") + " "
                + task.getName()
                + " completed");
        } catch (Exception e) {
            LOGGER.error("taskCompletedNoticeError", e);
        }
    }
}
