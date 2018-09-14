package com.proper.enterprise.platform.notice.server.app.dao.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.api.model.App;

import javax.persistence.*;

@Entity
@Table(name = "PEP_NOTICE_APP")
public class AppEntity extends BaseEntity implements App {

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

    /**
     * 描述
     */
    private String describe;



    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String getAppKey() {
        return appKey;
    }

    @Override
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String getAppToken() {
        return appToken;
    }

    @Override
    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    @Override
    public String getDescribe() {
        return describe;
    }

    @Override
    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
