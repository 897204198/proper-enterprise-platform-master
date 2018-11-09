package com.proper.enterprise.platform.notice.factory;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.service.NoticeCollector;

public class NoticeCollectorFactory {

    public static NoticeCollector create(NoticeType noticeType) {
        if (NoticeType.PUSH.equals(noticeType)) {
            return (NoticeCollector) PEPApplicationContext.getBean("noticePushCollector");
        } else if (NoticeType.EMAIL.equals(noticeType)) {
            return (NoticeCollector) PEPApplicationContext.getBean("noticeEmailCollector");
        } else if (NoticeType.SMS.equals(noticeType)) {
            return (NoticeCollector) PEPApplicationContext.getBean("noticeSmsCollector");
        } else {
            throw new ErrMsgException("can not find Notice Channel " + noticeType);
        }
    }

}
