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
     * 短信服务发送模板
     */
    private String smsSend;

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

    public String getSmsSend() {
        return smsSend;
    }

    public void setSmsSend(String smsSend) {
        this.smsSend = smsSend;
    }

    public String getSmsCharset() {
        return smsCharset;
    }

    public void setSmsCharset(String smsCharset) {
        this.smsCharset = smsCharset;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
