package com.proper.enterprise.platform.workflow.api;

import org.springframework.stereotype.Service;

@Service("assigneeExpression")
public class AssigneeExpression {

    public String assignee() {
        return "pep-sysadmin";
    }
}
