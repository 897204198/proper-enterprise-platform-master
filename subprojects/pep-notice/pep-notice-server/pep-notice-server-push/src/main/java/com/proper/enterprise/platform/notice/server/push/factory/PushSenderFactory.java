package com.proper.enterprise.platform.notice.server.push.factory;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;

public class PushSenderFactory {


    private PushSenderFactory() {
    }

    public static NoticeSendHandler product(PushChannelEnum pushChannel) {
        if (null == pushChannel) {
            throw new ErrMsgException("pushChannel can't be null");
        }
        switch (pushChannel) {
            case IOS:
                return (NoticeSendHandler) PEPApplicationContext.getBean("iosNoticeSender");
            case HUAWEI:
                return (NoticeSendHandler) PEPApplicationContext.getBean("huaweiNoticeSender");
            case XIAOMI:
                return (NoticeSendHandler) PEPApplicationContext.getBean("xiaomiNoticeSender");
            default:
                throw new ErrMsgException("pushChannel is not support");
        }
    }
}
