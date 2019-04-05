package com.proper.enterprise.platform.app.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PEP_APP_VERSIONS")
public class AppVersionDocument extends BaseDocument {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppVersionDocument.class);

    /**
     * 版本号
     */
    @Indexed
    @JsonProperty("ver")
    private String version;

    /**
     * apk 下载地址
     */
    @JsonProperty("androidUrl")
    private String androidURL;

    /**
     * ipa 下载地址
     */
    @JsonProperty("iosUrl")
    private String iosURL;

    /**
     * 版本说明
     */
    private String note;

    /**
     * 发布状态，true 代表已发布过
     */
    private boolean released;

    /**
     * 发布人
     */
    @JsonIgnore
    private String publisherId;

    @Transient
    private String publisher;

    /**
     * 发布时间
     */
    private String publishTime;

    /**
     * 是否强制更新
     */
    private Boolean forceUpdate;

    public AppVersionDocument() { }

    public AppVersionDocument(String version, String androidURL, String note) {
        this.version = version;
        this.androidURL = androidURL;
        this.note = note;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAndroidURL() {
        return androidURL;
    }

    public void setAndroidURL(String androidURL) {
        this.androidURL = androidURL;
    }

    public String getIosURL() {
        return iosURL;
    }

    public void setIosURL(String iosURL) {
        this.iosURL = iosURL;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public Boolean getForceUpdate() {
        if (forceUpdate == null) {
            return false;
        }
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getPublisher() {
        String publisher;
        try {
            publisher = PEPApplicationContext.getBean(UserService.class).get(publisherId).getUsername();
        } catch (Exception e) {
            LOGGER.debug("Could NOT get user name of {}, use user ID as publisher ID as fallback.", publisherId);
            publisher = publisherId;
        }
        return publisher;
    }

}
