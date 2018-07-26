package com.proper.enterprise.platform.workflow.rule;

import java.util.List;

public interface CandidateUserGroupByOrganizationRule {

    /**
     * 根据组织设定候选经办用户组
     *
     * @param organization 组织
     * @return 候选经办用户组Id
     */
    List<String> execute(String organization);
}
