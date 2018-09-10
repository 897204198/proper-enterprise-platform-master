package com.proper.enterprise.platform.notice.server.push.configurator.xiaomi;

import com.xiaomi.xmpush.server.Sender;

public interface XiaomiNoticeClient {
    /**
     *  通过密钥获取消息发送类
     * @param appSecret 密钥
     * @return 消息发送类
     */
    Sender getClient(String appSecret);
}
