package com.proper.enterprise.platform.notice.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Convert;

@Document(collection = "PEP_NOTICE")
public class NoticeDocument extends BaseDocument {

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
