package com.proper.enterprise.platform.notice.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_NOTICE")
@CacheEntity
public class NoticeEntity extends BaseEntity {

    /**
     * 系统ID
     */
    private String systemId;

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知正文
     */
    private String content;

    /**
     * 通知接收人ID 为空时代表广播通知
     */
    private String target;

    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private boolean isReaded;

    @Convert(converter = DataDicLiteConverter.class)
    private DataDicLiteBean noticeChannel;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setReaded(boolean readed) {
        isReaded = readed;
    }

    public DataDicLiteBean getNoticeChannel() {
        return noticeChannel;
    }

    public void setNoticeChannel(DataDicLiteBean noticeChannel) {
        this.noticeChannel = noticeChannel;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
}
