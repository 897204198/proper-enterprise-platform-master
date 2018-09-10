package com.proper.enterprise.platform.notice.server.push.configurator.ios;

import com.turo.pushy.apns.ApnsClient;

public interface IOSNoticeClient {

    /**
     * 根据IOS appKey获取客户端
     *
     * @param appKey 系统唯一标识
     * @return IOS客户端
     */
    ApnsClient getClient(String appKey);
}
