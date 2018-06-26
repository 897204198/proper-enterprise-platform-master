package com.proper.enterprise.platform.workflow.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.Date;

public class PEPProcessDefinitionVO {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PEPModelVO.class);

    private String key;
    private Integer version;
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

    @Override
    public String toString() {
        String pepProcessDefinitionVOStr = "";
        try {
            pepProcessDefinitionVOStr = JSONUtil.toJSON(this);
        } catch (IOException e) {
            LOGGER.error("PEPProcessDefinitionVO error , vo is null", e);
        }
        return pepProcessDefinitionVOStr;
    }
}
