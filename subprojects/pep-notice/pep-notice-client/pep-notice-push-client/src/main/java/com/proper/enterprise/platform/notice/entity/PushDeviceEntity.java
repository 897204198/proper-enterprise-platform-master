package com.proper.enterprise.platform.notice.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.notice.enums.PushDeviceType;

import javax.persistence.*;

@Entity
@Table(name = "PEP_PUSH_DEVICE")
@CacheEntity
public class PushDeviceEntity extends BaseEntity {

    /**
     * 应用标识
     */
    @Column(nullable = false)
    private String appKey;

    /**
     * 设备的唯一标识
     */
    private String deviceId;

    /**
     * 用户的唯一标识
     */
    private String userId;

    /**
     * 设备类型
     */
    @Enumerated(EnumType.STRING)
    private PushDeviceType deviceType;

    /**
     * 设备的其它信息
     */
    private String deviceOtherInfo;

    /**
     * 推送方式
     */
    private String pushMode;

    /**
     * 推送的token
     */
    private String pushToken;

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getDeviceOtherInfo() {
        return deviceOtherInfo;
    }

    public void setDeviceOtherInfo(String deviceOtherInfo) {
        this.deviceOtherInfo = deviceOtherInfo;
    }

    public String getPushMode() {
        return pushMode;
    }

    public void setPushMode(String pushMode) {
        this.pushMode = pushMode;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
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

    public PushDeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(PushDeviceType deviceType) {
        this.deviceType = deviceType;
    }
}
