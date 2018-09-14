package com.proper.enterprise.platform.notice.server.app.dao.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;

import javax.persistence.*;

@Entity
@Table(name = "PEP_NOTICE_APP")
public class AppEntity extends BaseEntity {

    public AppEntity() {
    }

    /**
     * 应用名称
     */
    @Column(nullable = false)
    private String appName;

    /**
     * 应用唯一标识
     */
    @Column(nullable = false, unique = true)
    private String appKey;

    /**
     * 应用token
     */
    @Column(nullable = false, unique = true)
    private String appToken;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

}
