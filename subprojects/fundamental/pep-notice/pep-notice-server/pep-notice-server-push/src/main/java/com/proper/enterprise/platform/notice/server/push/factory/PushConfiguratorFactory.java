package com.proper.enterprise.platform.notice.server.push.factory;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;

public class PushConfiguratorFactory {


    private PushConfiguratorFactory() {
    }

    public static NoticeConfigurator product(PushChannelEnum pushChannel) {
        if (null == pushChannel) {
            throw new ErrMsgException("pushChannel can't be null");
        }
        switch (pushChannel) {
            case APNS:
                return (NoticeConfigurator) PEPApplicationContext.getBean("iosNoticeConfigurator");
            case HUAWEI:
                return (NoticeConfigurator) PEPApplicationContext.getBean("huaweiNoticeConfigurator");
            case XIAOMI:
                return (NoticeConfigurator) PEPApplicationContext.getBean("xiaomiNoticeConfigurator");
            default:
                throw new ErrMsgException("pushChannel is not support");
        }
    }
}
