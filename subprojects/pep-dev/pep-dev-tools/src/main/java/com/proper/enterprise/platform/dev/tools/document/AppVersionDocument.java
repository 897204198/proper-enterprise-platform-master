package com.proper.enterprise.platform.dev.tools.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PEP_APP_VERSIONS")
public class AppVersionDocument extends BaseDocument {

    /**
     * 版本号
     */
    private long ver;

    /**
     * app 下载地址
     */
    private String url;

    /**
     * 版本说明
     */
    private String note;

    /**
     * 版本状态
     */
    private boolean valid = true;

    public long getVer() {
        return ver;
    }

    public void setVer(long ver) {
        this.ver = ver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
