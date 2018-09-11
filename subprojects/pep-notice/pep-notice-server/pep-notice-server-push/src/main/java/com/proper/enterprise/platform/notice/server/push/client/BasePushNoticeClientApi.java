package com.proper.enterprise.platform.notice.server.push.client;

import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;

public interface BasePushNoticeClientApi {

    /**
     * 新增客户端
     *
     * @param appKey   系统唯一标识
     * @param pushConf 系统配置
     */
    void post(String appKey, PushConfDocument pushConf);

    /**
     * 删除客户端
     *
     * @param appKey 系统唯一标识
     */
    void delete(String appKey);

    /**
     * 修改客户端
     *
     * @param appKey   系统唯一标识
     * @param pushConf 系统配置
     */
    void put(String appKey, PushConfDocument pushConf);
}
