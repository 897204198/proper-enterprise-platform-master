package com.proper.enterprise.platform.notice.server.app.vo;

import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.api.model.App;

public class AppVO extends BaseVO implements App {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用唯一标识
     */
    private String appKey;

    /**
     * 应用token
     */
    private String appToken;

    /**
     * 描述
     */
    private String describe;

    /**
     * app颜色
     */
    private String color;

    /**
     * 是否包含email配置
     */
    private Boolean haveEmailConf;

    /**
     * 是否包含短信配置
     */
    private Boolean haveSMSConf;

    /**
     * 是否包含推送配置
     */
    private Boolean havePushConf;

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String getAppKey() {
        return appKey;
    }

    @Override
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String getAppToken() {
        return appToken;
    }

    @Override
    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    @Override
    public String getDescribe() {
        return describe;
    }

    @Override
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getHaveEmailConf() {
        return haveEmailConf;
    }

    public void setHaveEmailConf(Boolean haveEmailConf) {
        this.haveEmailConf = haveEmailConf;
    }

    public Boolean getHaveSMSConf() {
        return haveSMSConf;
    }

    public void setHaveSMSConf(Boolean haveSMSConf) {
        this.haveSMSConf = haveSMSConf;
    }

    public Boolean getHavePushConf() {
        return havePushConf;
    }

    public void setHavePushConf(Boolean havePushConf) {
        this.havePushConf = havePushConf;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
