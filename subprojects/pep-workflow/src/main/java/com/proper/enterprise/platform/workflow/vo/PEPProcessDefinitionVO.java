package com.proper.enterprise.platform.workflow.vo;

import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class PEPProcessDefinitionVO {
    private String key;
    private int version;
    private String deploymentId;
    private Date deploymentTime;

    public PEPProcessDefinitionVO(ProcessDefinition processDefinition) {
        BeanUtils.copyProperties(processDefinition, this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getDeploymentTime() {
        if (null == deploymentTime) {
            return null;
        }
        return (Date) deploymentTime.clone();
    }

    public void setDeploymentTime(Date deploymentTime) {
        if (null == deploymentTime) {
            this.deploymentTime = null;
            return;
        }
        this.deploymentTime = (Date) deploymentTime.clone();
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }
}
