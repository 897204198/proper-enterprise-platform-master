package com.proper.enterprise.platform.workflow.rule;

import org.flowable.engine.delegate.DelegateExecution;

import java.util.List;

public interface CandidateByOrganizationRule {

    /**
     * 根据组织设定候选经办 人/用户组/角色 集合
     *
     * @param organization 组织
     * @return 候选经办 人/用户组/角色 集合
     */
    List<String> execute(String organization);

    /**
     * 设定候选经办 人/用户组/角色 集合
     *
     * @param delegateExecution 流程执行实例
     * @return 候选经办 人/用户组/角色 集合
     */
    List<String> executeByExecution(DelegateExecution delegateExecution);
}
