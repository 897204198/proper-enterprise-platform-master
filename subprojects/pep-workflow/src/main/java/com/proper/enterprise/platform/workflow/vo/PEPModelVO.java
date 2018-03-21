package com.proper.enterprise.platform.workflow.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;
import org.flowable.app.domain.editor.Model;
import org.springframework.beans.BeanUtils;

import javax.persistence.Convert;
import java.util.Date;

public class PEPModelVO {

    public enum ModelStatus {
        UN_DEPLOYED,
        DEPLOYED
    }

    private String id;
    private String name;
    private String key;
    private String description;
    private String createdBy;
    private String lastUpdatedBy;
    private Date lastUpdated;
    private Date created;
    private Date deploymentTime;
    private Integer modelVersion;
    private Integer processVersion;
    private String comment;
    private Integer modelType;
    @Convert(converter = DataDicLiteConverter.class)
    private DataDicLite status;

    public PEPModelVO() {

    }

    public PEPModelVO(Model model) {
        BeanUtils.copyProperties(model, this);
        this.setModelVersion(model.getVersion());
    }

    public PEPModelVO(String id, String name, Date deploymentTime, Integer processVersion) {
        this.id = id;
        this.name = name;
        this.setDeploymentTime(deploymentTime);
        this.processVersion = processVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getLastUpdated() {
        if (null == lastUpdated) {
            return null;
        }
        return (Date) lastUpdated.clone();
    }

    public void setLastUpdated(Date lastUpdated) {
        if (null == lastUpdated) {
            this.lastUpdated = null;
            return;
        }
        this.lastUpdated = (Date) lastUpdated.clone();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreated() {
        if (null == created) {
            return null;
        }
        return (Date) created.clone();
    }

    public void setCreated(Date created) {
        if (null == created) {
            this.created = null;
            return;
        }
        this.created = (Date) created.clone();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getDeploymentTime() {
        if (null == deploymentTime) {
            return null;
        }
        return (Date) deploymentTime.clone();
    }

    public void setDeploymentTime(Date deployTime) {
        if (null == deployTime) {
            this.deploymentTime = null;
            return;
        }
        this.deploymentTime = (Date) deployTime.clone();
    }

    public Integer getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(Integer modelVersion) {
        this.modelVersion = modelVersion;
    }

    public Integer getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(Integer processVersion) {
        this.processVersion = processVersion;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getModelType() {
        return modelType;
    }

    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }

    public DataDicLite getStatus() {
        return status;
    }

    public void setStatus(DataDicLite status) {
        this.status = status;
    }
}
