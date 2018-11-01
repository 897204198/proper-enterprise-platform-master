package com.proper.enterprise.platform.workflow.predicate;

import java.util.Set;

public class VariableQueryPredicate {

    private Set<String> taskIds;

    private Set<String> procInstIds;

    public Set<String> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(Set<String> taskIds) {
        this.taskIds = taskIds;
    }

    public Set<String> getProcInstIds() {
        return procInstIds;
    }

    public void setProcInstIds(Set<String> procInstIds) {
        this.procInstIds = procInstIds;
    }
}
