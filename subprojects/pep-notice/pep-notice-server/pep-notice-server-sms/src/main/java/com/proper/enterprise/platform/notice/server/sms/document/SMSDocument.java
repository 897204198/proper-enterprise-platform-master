package com.proper.enterprise.platform.notice.server.sms.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PEP_NOTICE_SMS")
public class SMSDocument extends BaseDocument {

    public SMSDocument() {
    }

    /**
     * 唯一标识
     */
    @Indexed(unique = true)
    private String appKey;

    /**
     * 短信服务地址
     */
    private String smsUrl;

    /**
     * 账号
     */
    private String userId;

    /**
     * 密码
     */
    private String password;

    /**
     * 短信服务发送模板
     */
    private String smsTemplate;

    /**
     * 短信服务发送编码
     */
    private String smsCharset;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public String getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(String smsTemplate) {
        this.smsTemplate = smsTemplate;
    }

    public String getSmsCharset() {
        return smsCharset;
    }

    public void setSmsCharset(String smsCharset) {
        this.smsCharset = smsCharset;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
