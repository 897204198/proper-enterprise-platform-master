package com.proper.enterprise.platform.notice.server.push.dao.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushDeviceTypeEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;

import javax.persistence.*;

@Entity
@Table(name = "PEP_PUSH_NOTICE_MSG")
public class PushNoticeMsgEntity extends BaseEntity {

    /**
     * 应用唯一标识
     */
    @Column(nullable = false)
    private String appKey;

    /**
     * 同一批消息的批次Id
     */
    @Column(length = 36)
    private String batchId;

    /**
     * 消息Id
     */
    @Column
    private String noticeId;

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
    @Column(nullable = false)
    private NoticeStatus status;

    /**
     * 消息发送次数
     */
    @Column(length = 3)
    private Integer sendCount;

    /**
     * 推送渠道
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PushChannelEnum pushChannel;

    /**
     * 设备类型
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PushDeviceTypeEnum deviceType;

    /**
     * 发送目标
     */
    @Column(nullable = false)
    private String targetTo;

    /**
     * 异常编码
     */
    private String errorCode;

    /**
     * 异常信息
     */
    @Column(length = 16777216)
    private String errorMsg;

    /**
     * 第三方唯一标识
     * 调用第三方接口返回的唯一标识 用于追踪消息状态
     */
    private String messageId;

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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
