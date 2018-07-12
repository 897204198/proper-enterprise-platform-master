package com.proper.enterprise.platform.workflow.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

public class PEPStartVO {

    /**
     * 流程定义名称
     */
    private String processDefinitionName;

    /**
     * 流程启动时间
     */
    private String createTime;

    /**
     * 流程启动者Id
     */
    private String startUserId;

    /**
     * 流程启动者名称
     */
    private String startUserName;
    /**
     * 开始节点表单
     */
    private PEPFormVO startForm;

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public PEPFormVO getStartForm() {
        return startForm;
    }

    public void setStartForm(PEPFormVO startForm) {
        this.startForm = startForm;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
