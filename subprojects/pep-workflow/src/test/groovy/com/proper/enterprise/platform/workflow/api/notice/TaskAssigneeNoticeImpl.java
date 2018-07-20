package com.proper.enterprise.platform.workflow.api.notice;

import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.workflow.api.TaskAssigneeNotice;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("taskAssigneeNoticePri")
@Primary
public class TaskAssigneeNoticeImpl implements TaskAssigneeNotice {

    @Override
    public void notice(TaskEntity task) {
        Authentication.setCurrentUserId(task.getAssignee());
    }
}
