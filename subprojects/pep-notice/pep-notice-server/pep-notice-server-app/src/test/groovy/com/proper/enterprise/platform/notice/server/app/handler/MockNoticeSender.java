package com.proper.enterprise.platform.notice.server.app.handler;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.app.global.SingletonMap;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.springframework.stereotype.Service;


@Service("mockNoticeSender")
public class MockNoticeSender implements NoticeSendHandler {
    public static final String MOCK_ERR_SEND = "mockErrSend";
    public static final String MOCK_PINDING_SEND = "mockPindingSend";
    private static final String MOCK_ERR_AFTER = "mockErrAfter";
    public static final String MOCK_RETRY_STATUS = "mockRetryStatus";
    private static final String MOCK_ERR_EXCEPTION = "mockErrException";

    @Override
    public BusinessNoticeResult send(ReadOnlyNotice notice) {
        if (MOCK_ERR_SEND.equals(notice.getAppKey())) {
            SingletonMap.getSingletMap().put(notice.getId(), NoticeStatus.FAIL);
            SingletonMap.getSingletMap().put(notice.getId() + "ErrMsg", "mock send Err");
            return new BusinessNoticeResult(NoticeStatus.FAIL, "mock send Err");
        }
        if (notice.getAppKey().startsWith(MOCK_PINDING_SEND)) {
            SingletonMap.getSingletMap().put(notice.getId(), NoticeStatus.PENDING);
            return new BusinessNoticeResult(NoticeStatus.PENDING);
        }
        if (MOCK_RETRY_STATUS.equals(notice.getAppKey())) {
            SingletonMap.getSingletMap().put(notice.getId(), NoticeStatus.RETRY);
            return new BusinessNoticeResult(NoticeStatus.RETRY);
        }
        SingletonMap.getSingletMap().put(notice.getId(), NoticeStatus.SUCCESS);
        return new BusinessNoticeResult(NoticeStatus.SUCCESS);
    }

    @Override
    public BusinessNoticeResult beforeSend(BusinessNotice notice) {
        notice.setTitle("bbb");
        return new BusinessNoticeResult(NoticeStatus.SUCCESS);
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
        NoticeStatus status = (NoticeStatus) SingletonMap.getSingletMap().get(notice.getId());
        if (null == status) {
            if (MOCK_ERR_SEND.equals(notice.getAppKey())) {
                return new BusinessNoticeResult(NoticeStatus.FAIL, "mock send Err");
            }
            if (notice.getAppKey().startsWith(MOCK_PINDING_SEND)) {
                return new BusinessNoticeResult(NoticeStatus.PENDING);
            }
            if (MOCK_RETRY_STATUS.equals(notice.getAppKey())) {
                return new BusinessNoticeResult(NoticeStatus.RETRY);
            }
            return new BusinessNoticeResult(NoticeStatus.SUCCESS);
        }
        if (NoticeStatus.PENDING == status) {
            int count = null == SingletonMap.getSingletMap()
                .get(notice.getId() + "count") ? 0 : (Integer) SingletonMap.getSingletMap()
                .get(notice.getId() + "count");
            String suffix = notice.getAppKey().replaceAll(MOCK_PINDING_SEND, "");
            if (StringUtil.isNotEmpty(suffix) && Integer.parseInt(suffix) <= count) {
                SingletonMap.getSingletMap().put(notice.getId(), NoticeStatus.SUCCESS);
                return new BusinessNoticeResult(NoticeStatus.SUCCESS);
            }
        }
        if (NoticeStatus.FAIL == status) {
            return new BusinessNoticeResult(NoticeStatus.FAIL, (String) SingletonMap.getSingletMap().get(notice.getId() + "ErrMsg"));
        }
        return new BusinessNoticeResult(status);
    }
}
