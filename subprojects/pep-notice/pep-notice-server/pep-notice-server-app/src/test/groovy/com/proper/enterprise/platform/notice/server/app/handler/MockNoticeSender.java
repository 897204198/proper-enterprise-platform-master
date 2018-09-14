package com.proper.enterprise.platform.notice.server.app.handler;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.app.global.SingletonMap;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.springframework.stereotype.Service;


@Service("mockNoticeSender")
public class MockNoticeSender implements NoticeSendHandler {
    private static final String MOCK_ERR_SEND = "mockErrSend";
    private static final String MOCK_ERR_AFTER = "mockErrAfter";
    private static final String MOCK_RETRY_STATUS = "mockRetryStatus";
    private static final String MOCK_ERR_EXCEPTION = "mockErrException";

    @Override
    public void send(ReadOnlyNotice notice) {
        if (MOCK_ERR_SEND.equals(notice.getAppKey())) {
            throw new ErrMsgException("mock send Err");
        }
        SingletonMap.getSingletMap().put(notice.getAppKey(), notice.getAppKey());
    }

    @Override
    public void beforeSend(BusinessNotice notice) {
        notice.setTitle("bbb");
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {
        if (MOCK_ERR_AFTER.equals(notice.getAppKey())) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
            SingletonMap.getSingletMap().put(MOCK_ERR_AFTER, "b");
        }
    }

    @Override
    public BusinessNoticeResult getStatus(ReadOnlyNotice notice) {
        if (MOCK_RETRY_STATUS.equals(notice.getAppKey())) {
            return new BusinessNoticeResult(NoticeStatus.FAIL, "error");
        }
        if (MOCK_ERR_EXCEPTION.equals(notice.getAppKey())) {
            throw new ErrMsgException(MOCK_ERR_EXCEPTION);
        }
        return new BusinessNoticeResult(NoticeStatus.SUCCESS);
    }
}
