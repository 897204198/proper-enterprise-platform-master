package com.proper.enterprise.platform.app.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_USER_APPLICATIONS")
public class UserApplicationEntity extends BaseEntity {

    /**
     * 用户ID
     */
    @Column(unique = true, nullable = false)
    private String userId;

    /**
     * 应用ID
     */
    @Column
    private String appId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
