package com.proper.enterprise.platform.notice.server.push.client.huawei;

import com.proper.enterprise.platform.notice.server.push.client.BasePushNoticeClientApi;

public interface HuaweiNoticeClientManagerApi extends BasePushNoticeClientApi {

    /**
     * 根据华为 appKey获取客户端
     *
     * @param appKey 系统唯一标识
     * @return access token
     */
    HuaweiNoticeClient get(String appKey);
}
