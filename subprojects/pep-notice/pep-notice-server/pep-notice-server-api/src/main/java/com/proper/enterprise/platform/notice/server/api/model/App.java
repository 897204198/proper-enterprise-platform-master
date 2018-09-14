package com.proper.enterprise.platform.notice.server.api.model;

import com.proper.enterprise.platform.core.api.IBase;

public interface App extends IBase {

    /**
     * 获取应用名称
     *
     * @return 应用名称
     */
    String getAppName();

    /**
     * 设置应用名称
     *
     * @param appName 应用名称
     */
    void setAppName(String appName);

    /**
     * 获取应用唯一标识
     *
     * @return 应用唯一标识
     */
    String getAppKey();

    /**
     * 设置应用唯一标识
     *
     * @param appKey 应用唯一标识
     */
    void setAppKey(String appKey);

    /**
     * 获取应用token
     *
     * @return 应用token
     */
    String getAppToken();

    /**
     * 设置应用token
     *
     * @param appToken 应用token
     */
    void setAppToken(String appToken);

    /**
     * 获取应用描述
     *
     * @return 应用描述
     */
    String getDescribe();

    /**
     * 设置应用描述
     *
     * @param describe 应用描述
     */
    void setDescribe(String describe);
}
