package com.proper.enterprise.platform.notice.server.push.configurator.xiaomi;

import com.xiaomi.xmpush.server.Sender;

public interface XiaomiNoticeClient {
    /**
     *  通过密钥获取消息发送类
     * @param appSecret 密钥
     * @return 消息发送类
     */
    Sender getClient(String appSecret);

    /**
     * 根据xiaomi appKey获取pushPackage
     *
     * @param appKey 系统唯一标识
     * @return pushPackage
     */
    String getPushPackage(String appKey);
}
