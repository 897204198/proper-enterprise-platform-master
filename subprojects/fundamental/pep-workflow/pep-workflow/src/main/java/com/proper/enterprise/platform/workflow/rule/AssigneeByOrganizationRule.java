package com.proper.enterprise.platform.workflow.rule;

import org.flowable.engine.delegate.DelegateExecution;

public interface AssigneeByOrganizationRule {

    /**
     * 根据组织设定经办人
     *
     * @param organization 组织
     * @return 经办人Id
     */
    String execute(String organization);

    /**
     * 设定经办人
     *
     * @param delegateExecution 流程执行实例
     * @return 经办人Id
     */
    String executeByExecution(DelegateExecution delegateExecution);
}
