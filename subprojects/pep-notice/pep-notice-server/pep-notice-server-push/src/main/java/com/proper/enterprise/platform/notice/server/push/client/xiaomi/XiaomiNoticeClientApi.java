package com.proper.enterprise.platform.notice.server.push.client.xiaomi;

import com.proper.enterprise.platform.notice.server.push.client.BasePushNoticeClientApi;
import com.xiaomi.xmpush.server.Sender;

public interface XiaomiNoticeClientApi extends BasePushNoticeClientApi {

    /**
     *  通过密钥获取消息发送类
     * @param appKey 应用标识
     * @return 消息发送类
     */
    Sender getClient(String appKey);


}
