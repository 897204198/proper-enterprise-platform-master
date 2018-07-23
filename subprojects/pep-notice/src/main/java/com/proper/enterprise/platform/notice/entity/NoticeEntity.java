package com.proper.enterprise.platform.notice.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_NOTICE")
@CacheEntity
public class NoticeEntity extends BaseEntity {

    /**
     *  业务ID
     */
    private String businessId;

    /**
     *  业务名称
     */
    private String businessName;

    /**
     *  通知标题
     */
    private String title;

    /**
     *  通知正文
     */
    private String content;

    /**
     *  通知接收人ID 为空时代表广播通知
     */
    private String toUserId;

    /**
     *  业务ID
     */
    private boolean isReaded;

    /**
     *  通知渠道
     */
    private String noticeChannelCode;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getNoticeChannelCode() {
        return noticeChannelCode;
    }

    public void setNoticeChannelCode(String noticeChannelCode) {
        this.noticeChannelCode = noticeChannelCode;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setReaded(boolean readed) {
        isReaded = readed;
    }
}
