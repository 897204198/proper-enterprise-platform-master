package com.proper.enterprise.platform.push.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.push.common.model.PushDevice;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceStatus;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;

@Entity
@Table(name = "PEP_PUSH_DEVICE")
@CacheEntity
public class PushDeviceEntity extends BaseEntity implements PushDevice {
    private static final long serialVersionUID = PEPConstants.VERSION;
    @Column(nullable = false)
    private String appkey;
    private String deviceid;
    private String userid;
    @Enumerated(EnumType.STRING)
    private PushDeviceType devicetype;
    private String otherInfo;
    @Enumerated(EnumType.STRING)
    private PushMode pushMode;
    /**
     * 推送标识
     */
    private String pushToken;
    @Enumerated(EnumType.ORDINAL)
    private PushDeviceStatus mstatus;

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public PushDeviceType getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(PushDeviceType devicetype) {
        this.devicetype = devicetype;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public PushMode getPushMode() {
        return pushMode;
    }

    public void setPushMode(PushMode pushMode) {
        this.pushMode = pushMode;
    }

    public PushDeviceStatus getMstatus() {
        return mstatus;
    }

    public void setMstatus(PushDeviceStatus mstatus) {
        this.mstatus = mstatus;
    }

}
