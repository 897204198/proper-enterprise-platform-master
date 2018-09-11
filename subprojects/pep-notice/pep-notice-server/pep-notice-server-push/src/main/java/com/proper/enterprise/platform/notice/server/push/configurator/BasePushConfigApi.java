package com.proper.enterprise.platform.notice.server.push.configurator;

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;

public interface BasePushConfigApi extends NoticeConfigurator {


    /**
     * 根据appKey和推送渠道获取pushPackage
     *
     * @param appKey      系统唯一标识
     * @param pushChannel 推送渠道
     * @return pushPackage
     */
    String getPushPackage(String appKey, PushChannelEnum pushChannel);

    /**
     * 根据appKey和推送渠道获取pushConf
     *
     * @param appKey      系统唯一标识
     * @param pushChannel 推送渠道
     * @return pushConf
     */
    PushConfDocument getConf(String appKey, PushChannelEnum pushChannel);

}
