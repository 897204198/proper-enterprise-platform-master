package com.proper.enterprise.platform.notice.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_NOTICE_SET")
@CacheEntity
public class NoticeSetEntity extends BaseEntity {

    /**
     *  用户ID
     */
    private String userId;

    /**
     *  通知类型
     */
    private String noticeType;

    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private boolean isPush;

    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private boolean isSms;

    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private boolean isEmail;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public boolean isPush() {
        return isPush;
    }

    public void setPush(boolean push) {
        isPush = push;
    }

    public boolean isSms() {
        return isSms;
    }

    public void setSms(boolean sms) {
        isSms = sms;
    }

    public boolean isEmail() {
        return isEmail;
    }

    public void setEmail(boolean email) {
        isEmail = email;
    }
}
