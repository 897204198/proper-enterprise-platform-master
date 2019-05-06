package com.proper.enterprise.platform.workflow.vo;

import com.fasterxml.jackson.annotation.JsonView;

public class PEPProcInstVO {

    /**
     * 流程实例Id
     */
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String procInstId;

    /**
     * 流程定义Id
     */
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String processDefinitionId;

    /**
     * 流程定义key
     */
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String processDefinitionKey;

    /**
     * 流程定义名称
     */
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String processDefinitionName;

    /**
     * 流程标题
     */
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String processTitle;

    /**
     * 流程启动时间
     */
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String createTime;

    /**
     * 流程结束时间
     */
    private String endTime;


    /**
     * 流程启动者Id
     */
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String startUserId;

    /**
     * 流程启动者名称
     */
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String startUserName;

    /**
     * 是否结束  true是 false否
     */
    private Boolean ended;

    /**
     * 流程状态
     */
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String stateCode;
    /**
     * 流程状态
     */
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String stateValue;

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getProcessTitle() {
        return processTitle;
    }

    public void setProcessTitle(String processTitle) {
        this.processTitle = processTitle;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public Boolean getEnded() {
        return ended;
    }

    public void setEnded(Boolean ended) {
        this.ended = ended;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateValue() {
        return stateValue;
    }

    public void setStateValue(String stateValue) {
        this.stateValue = stateValue;
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    @Override
    public String toString() {
        return "PEPProcInstVO{"
            + "procInstId='"
            + procInstId
            + '\'' + ", processDefinitionId='" + processDefinitionId + '\'' + ", processDefinitionKey='"
            + processDefinitionKey + '\'' + ", processDefinitionName='" + processDefinitionName + '\''
            + ", createTime='" + createTime + '\'' + ", endTime='" + endTime + '\'' + ", startUserId='"
            + startUserId + '\'' + ", startUserName='" + startUserName + '\'' + ", ended="
            + ended + ", state='" + stateCode + '\'' + '}';
    }
}
