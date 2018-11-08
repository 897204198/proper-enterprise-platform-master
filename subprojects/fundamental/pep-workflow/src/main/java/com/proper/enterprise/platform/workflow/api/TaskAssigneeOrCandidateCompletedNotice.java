package com.proper.enterprise.platform.workflow.api;

import org.flowable.task.service.impl.persistence.entity.TaskEntity;

public interface TaskAssigneeOrCandidateCompletedNotice {

    /**
     * 经办人通知
     *
     * @param task 任务对象
     */
    void notice(TaskEntity task);
}
