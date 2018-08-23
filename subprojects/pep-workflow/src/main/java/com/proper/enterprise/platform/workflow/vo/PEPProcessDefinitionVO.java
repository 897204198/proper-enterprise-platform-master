package com.proper.enterprise.platform.workflow.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Map;

public class PEPProcessDefinitionVO {

    private String id;
    private String key;
    private String name;
    private Integer version;
    private String deploymentId;
    private Date deploymentTime;
    private String startFormKey;
    private Map<String, PEPPropertyVO> formProperties;

    public PEPProcessDefinitionVO() {
    }

    public PEPProcessDefinitionVO(ProcessDefinition processDefinition) {
        BeanUtils.copyProperties(processDefinition, this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStartFormKey() {
        return startFormKey;
    }

    public void setStartFormKey(String startFormKey) {
        this.startFormKey = startFormKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, PEPPropertyVO> getFormProperties() {
        return formProperties;
    }

    public void setFormProperties(Map<String, PEPPropertyVO> formProperties) {
        this.formProperties = formProperties;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
