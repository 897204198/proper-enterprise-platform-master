package com.proper.enterprise.platform.notice.server.push.vo;

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
    private String appDesc;

    /**
     * 应用的消息总数
     */
    private Integer channelCount;

    public Integer getChannelCount() {
        if (null == channelCount) {
            channelCount = 0;
        }
        return channelCount;
    }

    public void setChannelCount(Integer channelCount) {
        this.channelCount = channelCount;
    }

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
    public String getAppDesc() {
        return appDesc;
    }

    @Override
    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
