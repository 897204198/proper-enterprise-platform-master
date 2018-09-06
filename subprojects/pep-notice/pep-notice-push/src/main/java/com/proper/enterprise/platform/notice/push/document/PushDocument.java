package com.proper.enterprise.platform.notice.push.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PEP_NOTICE_PUSH")
public class PushDocument extends BaseDocument {

    public PushDocument() {
    }

    /**
     * 唯一标识
     */
    @Indexed(unique = true)
    private String appKey;

    /**
     * 推送密钥
     */
    private String pushSecret;

    /**
     * 推送包名
     */
    private String pushPackageName;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getPushSecret() {
        return pushSecret;
    }

    public void setPushSecret(String pushSecret) {
        this.pushSecret = pushSecret;
    }

    public String getPushPackageName() {
        return pushPackageName;
    }

    public void setPushPackageName(String pushPackageName) {
        this.pushPackageName = pushPackageName;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
