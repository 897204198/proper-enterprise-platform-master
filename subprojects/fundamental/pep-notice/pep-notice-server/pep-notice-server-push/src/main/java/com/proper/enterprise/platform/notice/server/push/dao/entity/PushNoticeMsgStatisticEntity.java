package com.proper.enterprise.platform.notice.server.push.dao.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;

import javax.persistence.*;

@Entity
@Table(name = "PEP_PUSH_NOTICE_STATISTIC")
public class PushNoticeMsgStatisticEntity extends BaseEntity {

    @Column(nullable = false)
    private String appKey;

    /**
     * 渠道
     */
    @Enumerated(EnumType.STRING)
    private PushChannelEnum pushChannel;

    /**
     * 消息成功发送日期或者最后修改日期
     */
    private String sendDate;

    /**
     * 消息状态
     */
    @Enumerated(EnumType.STRING)
    private NoticeStatus status;

    /**
     * 消息数量
     */
    private Integer msgCount;

    /**
     * 属于第几周
     */
    private String week;

    /**
     * 属于第几月
     */
    private String month;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Integer getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(Integer msgCount) {
        this.msgCount = msgCount;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public PushChannelEnum getPushChannel() {
        return pushChannel;
    }

    public void setPushChannel(PushChannelEnum pushChannel) {
        this.pushChannel = pushChannel;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public NoticeStatus getStatus() {
        return status;
    }

    public void setStatus(NoticeStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
