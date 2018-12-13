package com.proper.enterprise.platform.notice.server.push.mock;

import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.enums.huawei.HuaweiErrCodeEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MockRetryNotice {

    /**
     * 华为返回503
     */
    private static final String ERR_503 = "503 Service Temporarily Unavailable";

    public BusinessNoticeResult isSuccess(ResponseEntity<byte[]> res, ReadOnlyNotice notice) {
        if (res.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            return new BusinessNoticeResult(NoticeStatus.RETRY,
                HuaweiErrCodeEnum.convertErrorCode(ERR_503), ERR_503);
        }
        return new BusinessNoticeResult(NoticeStatus.SUCCESS);
    }
}
