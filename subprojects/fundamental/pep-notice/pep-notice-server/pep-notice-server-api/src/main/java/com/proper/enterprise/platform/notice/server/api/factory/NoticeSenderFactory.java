package com.proper.enterprise.platform.notice.server.api.factory;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;

public class NoticeSenderFactory {

    private NoticeSenderFactory() {
    }

    public static NoticeSendHandler product(NoticeType noticeType) {
        if (null == noticeType) {
            throw new ErrMsgException("noticeType can't be null");
        }
        switch (noticeType) {
            case SMS:
                return (NoticeSendHandler) PEPApplicationContext.getBean("smsNoticeSender");
            case PUSH:
                return (NoticeSendHandler) PEPApplicationContext.getBean("pushNoticeSender");
            case EMAIL:
                return (NoticeSendHandler) PEPApplicationContext.getBean("emailNoticeSender");
            case MOCK:
                return (NoticeSendHandler) PEPApplicationContext.getBean("mockNoticeSender");
            default:
                throw new ErrMsgException("noticeType is not support");
        }
    }
}
