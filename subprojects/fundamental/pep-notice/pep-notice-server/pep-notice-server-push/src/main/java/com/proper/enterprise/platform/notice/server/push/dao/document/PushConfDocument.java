package com.proper.enterprise.platform.notice.server.push.dao.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushProfileEnum;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PEP_NOTICE_PUSH_CONF")
@CompoundIndex(unique = true, name = "UK_APPKEY_PUSHCHANNEL", def = "{'appKey': 1, 'pushChannel': -1}")
public class PushConfDocument extends BaseDocument {

    public PushConfDocument() {
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
     * 推送应用ID(华为)
     */
    private String appId;

    /**
     * 推送密钥或IOS的证书密码
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

    /**
     * 证书密码
     */
    private String certPassword;

    /**
     * 推送环境配置(DEV/PRODUCTION IOS需要)
     */
    private PushProfileEnum pushProfile;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    public PushProfileEnum getPushProfile() {
        return pushProfile;
    }

    public void setPushProfile(PushProfileEnum pushProfile) {
        this.pushProfile = pushProfile;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
