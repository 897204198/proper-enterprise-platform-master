package com.proper.enterprise.platform.notice.server.push.sender;

import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.factory.PushSenderFactory;
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
    public BusinessNoticeResult getStatus(ReadOnlyNotice notice) throws NoticeException {
        return PushSenderFactory.product(getPushChannel(notice)).getStatus(notice);
    }
}
