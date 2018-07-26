package com.proper.enterprise.platform.workflow.rule;

import java.util.List;

public interface CandidateByOrganizationRule {

    /**
     * 根据组织设定候选经办 人/用户组/角色 集合
     *
     * @param organization 组织
     * @return 候选经办 人/用户组/角色 集合
     */
    List<String> execute(String organization);
}
