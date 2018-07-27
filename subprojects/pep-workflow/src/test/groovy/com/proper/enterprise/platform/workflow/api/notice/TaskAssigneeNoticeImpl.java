package com.proper.enterprise.platform.workflow.api.notice;

import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.workflow.api.TaskAssigneeOrCandidateNotice;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("taskAssigneeNoticePri")
@Primary
public class TaskAssigneeNoticeImpl implements TaskAssigneeOrCandidateNotice {

    @Override
    public void notice(TaskEntity task) {
        StringBuffer returnValue = new StringBuffer();
        for (IdentityLinkEntity identityLinkEntity : task.getIdentityLinks()) {
            if (identityLinkEntity.isUser()) {
                returnValue.append(identityLinkEntity.getUserId());
            }
            if (identityLinkEntity.isGroup()) {
                returnValue.append(identityLinkEntity.getGroupId());
            }
            if (identityLinkEntity.isRole()) {
                returnValue.append(identityLinkEntity.getRoleId());
            }
        }
        Authentication.setCurrentUserId(task.getAssignee() + returnValue);
    }
}
