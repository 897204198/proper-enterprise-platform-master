package com.proper.enterprise.platform.app.controller.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import io.swagger.annotations.ApiModelProperty;

public class AppVersionVO {

    @ApiModelProperty("‍版本号")
    private String version;

    @ApiModelProperty("‍android下载地址")
    private String androidURL;

    @ApiModelProperty("‍ios下载地址")
    private String iosURL;

    @ApiModelProperty("‍版本说明")
    private String note;

    public AppVersionVO() { }

    public AppVersionVO(String version, String androidURL, String note) {
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

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

}
