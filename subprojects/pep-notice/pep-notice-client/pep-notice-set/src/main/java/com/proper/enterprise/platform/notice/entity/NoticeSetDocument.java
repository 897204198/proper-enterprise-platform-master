package com.proper.enterprise.platform.notice.entity;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PEP_NOTICE_SET")
public class NoticeSetDocument extends BaseDocument {

    public NoticeSetDocument() {
    }

    public NoticeSetDocument(boolean isPush, boolean isEmail, boolean isSms) {
        this.isPush = isPush;
        this.isEmail = isEmail;
        this.isSms = isSms;
    }

    /**
     *  用户ID
     */
    private String userId;

    /**
     *  目录
     */
    private String catalog;

    private boolean isPush;

    private boolean isSms;

    private boolean isEmail;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
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
