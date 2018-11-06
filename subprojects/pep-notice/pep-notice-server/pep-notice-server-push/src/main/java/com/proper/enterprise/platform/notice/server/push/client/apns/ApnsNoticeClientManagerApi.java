package com.proper.enterprise.platform.notice.server.push.client.apns;

import com.proper.enterprise.platform.notice.server.push.client.BasePushNoticeClientApi;
import com.turo.pushy.apns.ApnsClient;

public interface ApnsNoticeClientManagerApi extends BasePushNoticeClientApi {

    /**
     * 根据IOS appKey获取客户端
     *
     * @param appKey 系统唯一标识
     * @return IOS客户端
     */
    ApnsClient get(String appKey);

}
