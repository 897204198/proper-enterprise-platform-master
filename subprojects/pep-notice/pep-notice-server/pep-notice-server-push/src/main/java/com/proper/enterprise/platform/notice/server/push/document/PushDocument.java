package com.proper.enterprise.platform.notice.server.push.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PEP_NOTICE_PUSH")
@CompoundIndex(unique = true, name = "UK_APPKEY_PUSHCHANNEL", def = "{'appKey': 1, 'pushChannel': -1}")
public class PushDocument extends BaseDocument {

    public PushDocument() {
    }

    /**
     * 唯一标识
     */
    private String appKey;

    /**
     * 推送渠道
     */
    private PushChannelEnum pushChannel;

    /**
     * 推送密钥
     */
    private String appSecret;

    /**
     * 推送包名
     */
    private String pushPackage;

    /**
     * 证书Id
     */
    private String certificateId;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getPushPackage() {
        return pushPackage;
    }

    public void setPushPackage(String pushPackage) {
        this.pushPackage = pushPackage;
    }

    public PushChannelEnum getPushChannel() {
        return pushChannel;
    }

    public void setPushChannel(PushChannelEnum pushChannel) {
        this.pushChannel = pushChannel;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
