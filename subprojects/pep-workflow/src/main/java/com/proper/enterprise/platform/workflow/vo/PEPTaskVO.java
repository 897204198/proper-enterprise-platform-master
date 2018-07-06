package com.proper.enterprise.platform.workflow.vo;

import java.util.Map;

public class PEPTaskVO {

    /**
     * 任务Id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 当前经办人
     */
    private String assignee;
    /**
     * 当前经办人名称
     */
    private String assigneeName;
    /**
     * 任务表单
     */
    private Map<String, Object> variables;
    /**
     * 任务对应的formKey
     */
    private String formKey;
    /**
     * 任务开始时间
     */
    private String createTime;
    /**
     * 任务结束时间
     */
    private String endTime;
    /**
     * 流程实例Id
     */
    private String procInstId;
    /**
     * 流程实例信息
     */
    private PEPProcInstVO pepProcInstVO;

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PEPProcInstVO getPepProcInstVO() {
        return pepProcInstVO;
    }

    public void setPepProcInstVO(PEPProcInstVO pepProcInstVO) {
        this.pepProcInstVO = pepProcInstVO;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    @Override
    public String toString() {
        return "PEPTaskVO{taskId='" + taskId + '\'' + ", name='" + name + '\'' + ", assignee='" + assignee + '\''
            + ", variables=" + variables + ", formKey='" + formKey + '\'' + ", createTime='" + createTime + '\''
            + ", procInstId='" + procInstId + '\'' + ", pepProcInstVO=" + pepProcInstVO + '}';
    }
}
