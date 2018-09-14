package com.proper.enterprise.platform.notice.server.api.service;

import com.proper.enterprise.platform.notice.server.api.model.App;

public interface AppDaoService {

    /**
     * 保存应用
     *
     * @param app 应用
     * @return 应用token
     */
    App save(App app);

    /**
     * 刷新应用token
     *
     * @param appKey 应用唯一标识
     * @return 应用token
     */
    App updateAppToken(String appKey);

    /**
     * 应用起停用
     *
     * @param appKey 应用唯一标识
     * @param enable true启用 false停用
     * @return 应用token
     */
    App updateAppStatus(String appKey, boolean enable);

    /**
     * 修改应用名称
     *
     * @param appKey  应用唯一标识
     * @param appName 应用名称
     * @return 应用token
     */
    App updateAppName(String appKey, String appName);

    /**
     * 修改应用描述
     *
     * @param appKey   应用唯一标识
     * @param describe 应用描述
     * @return 应用token
     */
    App updateAppDescribe(String appKey, String describe);
}
