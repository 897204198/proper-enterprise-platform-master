package com.proper.enterprise.platform.workflow.model;

public class PEPWorkflowNoticeUrlParam {

    /**
     * 是否为开始节点
     */
    private boolean isLaunch;
    /**
     * 任务Id
     */
    private String taskOrProcDefKey;
    /**
     * 流程实例Id
     */
    private String procInstId;
    /**
     * name
     */
    private String name;
    /**
     * 状态
     */
    private String stateCode;
    /**
     * 业务参数
     */
    private PEPWorkflowNoticeUrlBusinessParam businessObj;

    public boolean isLaunch() {
        return isLaunch;
    }

    public void setLaunch(boolean launch) {
        isLaunch = launch;
    }

    public String getTaskOrProcDefKey() {
        return taskOrProcDefKey;
    }

    public void setTaskOrProcDefKey(String taskOrProcDefKey) {
        this.taskOrProcDefKey = taskOrProcDefKey;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public PEPWorkflowNoticeUrlBusinessParam getBusinessObj() {
        return businessObj;
    }

    public void setBusinessObj(PEPWorkflowNoticeUrlBusinessParam businessObj) {
        this.businessObj = businessObj;
    }
}
