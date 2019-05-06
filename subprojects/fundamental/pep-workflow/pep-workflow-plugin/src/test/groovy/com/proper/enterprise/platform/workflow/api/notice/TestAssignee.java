package com.proper.enterprise.platform.workflow.api.notice;

import org.springframework.stereotype.Service;

@Service("testAssignee")
public class TestAssignee {

    public String assignee() {
        return "PEP_SYS";
    }
}
