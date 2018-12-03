package com.proper.enterprise.platform.feedback.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_PROBLEM_RECORD")
public class RecordEntity extends BaseEntity {

    /**
     * 问题id
     */
    private String problemId;

    /**
     * 浏览标识
     */
    private String deviceId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 评价(赞or踩)
     */
    private String assess;

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAssess() {
        return assess;
    }

    public void setAssess(String assess) {
        this.assess = assess;
    }
}
