package com.proper.enterprise.platform.notice.server.push.handler;

import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.factory.PushSenderFactory;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.springframework.stereotype.Service;

@Service("pushNoticeSender")
public class PushNoticeSender extends AbstractPushSendSupport implements NoticeSendHandler {

    @Override
    public void send(ReadOnlyNotice notice) throws NoticeException {
        PushSenderFactory.product(getPushChannel(notice)).send(notice);
    }

    @Override
    public void beforeSend(BusinessNotice notice) throws NoticeException {
        PushSenderFactory.product(getPushChannel(notice)).beforeSend(notice);
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {
        PushSenderFactory.product(getPushChannel(notice)).afterSend(notice);
    }

    @Override
    public NoticeStatus getStatus(ReadOnlyNotice notice) throws NoticeException {
        return PushSenderFactory.product(getPushChannel(notice)).getStatus(notice);
    }
}
