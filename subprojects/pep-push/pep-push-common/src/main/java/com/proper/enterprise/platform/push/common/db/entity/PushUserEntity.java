package com.proper.enterprise.platform.push.common.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.push.common.model.PushUser;

@Entity
@Table(name = "PP_USER")
@CacheEntity
public class PushUserEntity extends BaseEntity implements PushUser {
    private static final long serialVersionUID = PEPConstants.VERSION;

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
