package com.proper.enterprise.platform.workflow.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.view.BaseView;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.model.PEPTaskCandidate;

import java.util.List;
import java.util.Map;

public class PEPTaskVO {

    public interface ToDoView extends BaseView {

    }

    /**
     * 任务Id
     */
    @JsonView(value = {ToDoView.class})
    private String taskId;
    /**
     * 任务名称
     */
    @JsonView(value = {ToDoView.class})
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
     * 候选集合展示
     */
    private List<PEPTaskCandidate> candidates;

    /**
     * 任务表单
     */
    @JsonView(value = {ToDoView.class})
    private PEPForm form;

    /**
     * 全局变量
     */
    @JsonView(value = {ToDoView.class})
    private Map<String, Object> globalData;

    /**
     * 任务开始时间
     */
    @JsonView(value = {ToDoView.class})
    private String createTime;
    /**
     * 任务结束时间
     */
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String endTime;
    /**
     * 流程实例Id
     */
    private String procInstId;
    /**
     * 流程实例信息
     */
    @JsonView(value = {ToDoView.class})
    private PEPProcInstVO pepProcInst;

    private Boolean sameAssigneeSkip;

    public PEPForm getForm() {
        return form;
    }

    public void setForm(PEPForm form) {
        this.form = form;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
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

    public PEPProcInstVO getPepProcInst() {
        return pepProcInst;
    }

    public void setPepProcInst(PEPProcInstVO pepProcInst) {
        this.pepProcInst = pepProcInst;
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

    public Boolean getSameAssigneeSkip() {
        return sameAssigneeSkip;
    }

    public void setSameAssigneeSkip(Boolean sameAssigneeSkip) {
        this.sameAssigneeSkip = sameAssigneeSkip;
    }

    public Map<String, Object> getGlobalData() {
        return globalData;
    }

    public void setGlobalData(Map<String, Object> globalData) {
        this.globalData = globalData;
    }

    public List<PEPTaskCandidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<PEPTaskCandidate> candidates) {
        this.candidates = candidates;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
