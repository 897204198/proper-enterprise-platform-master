package com.proper.enterprise.platform.workflow.rule;

public interface AssigneeByOrganizationRule {

    /**
     * 根据组织设定经办人
     *
     * @param organization 组织
     * @return 经办人Id
     */
    String execute(String organization);
}
