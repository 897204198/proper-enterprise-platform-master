package com.proper.enterprise.platform.workflow.vo;

import java.util.List;

public class CustomHandlerVO {

    /**
     * 经办人
     */
    private String assignee;
    /**
     * 候选人集合
     */
    private List<String> candidateUsers;

    /**
     * 候选角色集合
     */
    private List<String> candidateRoles;
    /**
     * 候选用户组集合
     */
    private List<String> candidateGroups;

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public List<String> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<String> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public List<String> getCandidateRoles() {
        return candidateRoles;
    }

    public void setCandidateRoles(List<String> candidateRoles) {
        this.candidateRoles = candidateRoles;
    }

    public List<String> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(List<String> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    @Override
    public String toString() {
        return "CustomHandlerVO{assignee='" + assignee + '\'' + ", candidateUsers="
            + candidateUsers + ", candidateRoles=" + candidateRoles + ", candidateGroups=" + candidateGroups + '}';
    }
}
