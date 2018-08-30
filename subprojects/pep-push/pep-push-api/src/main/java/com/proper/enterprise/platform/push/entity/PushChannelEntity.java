package com.proper.enterprise.platform.push.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_PUSH_CHANNEL")
public class PushChannelEntity extends BaseEntity {

    @Column(nullable = false)
    private String channelName;

    @Column(nullable = false)
    private String channelDesc;

    private int msgSaveDays;

    private int maxSendCount;

    @Column(nullable = false)
    private String secretKey;

    @Column
    private String android;

    @Column
    private String ios;

    @Column
    private String diplomaId;

    @Column
    private String color;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelDesc() {
        return channelDesc;
    }

    public void setChannelDesc(String channelDesc) {
        this.channelDesc = channelDesc;
    }

    public int getMsgSaveDays() {
        return msgSaveDays;
    }

    public void setMsgSaveDays(int msgSaveDays) {
        this.msgSaveDays = msgSaveDays;
    }

    public int getMaxSendCount() {
        return maxSendCount;
    }

    public void setMaxSendCount(int maxSendCount) {
        this.maxSendCount = maxSendCount;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }

    public String getIos() {
        return ios;
    }

    public void setIos(String ios) {
        this.ios = ios;
    }

    public String getDiplomaId() {
        return diplomaId;
    }

    public void setDiplomaId(String diplomaId) {
        this.diplomaId = diplomaId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
