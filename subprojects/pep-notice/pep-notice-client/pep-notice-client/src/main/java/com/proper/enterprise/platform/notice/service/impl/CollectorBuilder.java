package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.notice.server.api.enums.NoticeType;
import com.proper.enterprise.platform.notice.service.NoticeCollector;

public class CollectorBuilder {

    public static NoticeCollector create(NoticeType noticeType) {
        if (noticeType.equals(NoticeType.PUSH)) {
            return (NoticeCollector) PEPApplicationContext.getBean("noticePushCollector");
        } else if (noticeType.equals(NoticeType.EMAIL)) {
            return (NoticeCollector) PEPApplicationContext.getBean("noticeEmailCollector");
        } else if (noticeType.equals(NoticeType.SMS)) {
            return (NoticeCollector) PEPApplicationContext.getBean("noticeSmsCollector");
        } else {
            throw new ErrMsgException("can not find Notice Channel " + noticeType);
        }
    }

}
