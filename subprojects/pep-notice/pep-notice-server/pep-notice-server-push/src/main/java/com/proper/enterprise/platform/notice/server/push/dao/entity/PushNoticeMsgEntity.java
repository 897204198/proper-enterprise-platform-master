package com.proper.enterprise.platform.notice.server.push.dao.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.enums.PushDeviceTypeEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PEP_PUSH_NOTICE_MSG")
public class PushNoticeMsgEntity extends BaseEntity {

    @Column(nullable = false)
    private String appKey;

    /**
     * 消息正文
     */
    @Column(nullable = false, length = 2048)
    private String content;
    /**
     * 消息标题
     */
    @Column(length = 36)
    private String title;

    /**
     * 消息状态，是否发送
     */
    @Enumerated(EnumType.STRING)
    private NoticeStatus status;
    /**
     * 消息成功发送日期
     */
    private Date sendDate;
    /**
     * 消息发送次数
     */
    private Integer sendCount;

    /**
     * 同一批消息的批次Id
     */
    @Column(length = 36)
    private String batchId;

    /**
     * 消息Id
     */
    @Column(length = 36)
    private String noticeId;

    /**
     * 推送渠道
     */
    @Enumerated(EnumType.STRING)
    private PushChannelEnum pushChannel;

    /**
     * 设备类型
     */
    @Enumerated(EnumType.STRING)
    private PushDeviceTypeEnum deviceType;

    @Column(nullable = false)
    private String targetTo;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NoticeStatus getStatus() {
        return status;
    }

    public void setStatus(NoticeStatus status) {
        this.status = status;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public PushChannelEnum getPushChannel() {
        return pushChannel;
    }

    public void setPushChannel(PushChannelEnum pushChannel) {
        this.pushChannel = pushChannel;
    }

    public PushDeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(PushDeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
    }

    public String getTargetTo() {
        return targetTo;
    }

    public void setTargetTo(String targetTo) {
        this.targetTo = targetTo;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
