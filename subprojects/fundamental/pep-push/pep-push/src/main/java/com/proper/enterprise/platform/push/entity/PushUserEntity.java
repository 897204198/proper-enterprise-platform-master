package com.proper.enterprise.platform.push.entity;

import com.proper.enterprise.platform.core.PEPVersion;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.push.common.model.PushUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_PUSH_USER")
@CacheEntity
public class PushUserEntity extends BaseEntity implements PushUser {
    private static final long serialVersionUID = PEPVersion.VERSION;

    @Column(nullable = false)
    private String appkey;
    @Column(nullable = false)
    private String userid;
    @Column(nullable = false)
    private String otherInfo;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

}
