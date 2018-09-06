package com.proper.enterprise.platform.notice.server.app.factory;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;

public class NoticeConfiguratorFactory {

    private NoticeConfiguratorFactory() {
    }

    public static NoticeConfigurator product(NoticeType noticeType) {
        if (null == noticeType) {
            throw new ErrMsgException("noticeType can't be null");
        }
        switch (noticeType) {
            case SMS:
                return (NoticeConfigurator) PEPApplicationContext.getBean("smsNoticeConfigurator");
            case PUSH:
                return (NoticeConfigurator) PEPApplicationContext.getBean("pushNoticeConfigurator");
            case EMAIL:
                return (NoticeConfigurator) PEPApplicationContext.getBean("emailNoticeConfigurator");
            case MOCK:
                return (NoticeConfigurator) PEPApplicationContext.getBean("mockNoticeConfigurator");
            default:
                throw new ErrMsgException("noticeType is not support");
        }
    }
}
