package com.proper.enterprise.platform.notice.server.sms.configurator;

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;

import java.util.Map;

public interface SMSConfigurator extends NoticeConfigurator {
    /**
     * 获取配置
     *
     * @param appKey 应用唯一标识
     * @return 配置信息
     */
    Map get(String appKey);
}
