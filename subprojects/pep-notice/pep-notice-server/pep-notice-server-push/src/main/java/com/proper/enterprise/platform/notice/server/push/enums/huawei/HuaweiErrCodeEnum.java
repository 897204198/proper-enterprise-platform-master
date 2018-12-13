package com.proper.enterprise.platform.notice.server.push.enums.huawei;

import com.proper.enterprise.platform.notice.server.sdk.constants.NoticeErrorCodeConstants;

public enum HuaweiErrCodeEnum {

    /**
     * 无效目标
     */
    INVALID_TARGET("All the tokens are invalid", NoticeErrorCodeConstants.INVALID_TARGET),

    /**
     * 无效目标
     */
    SERVICE_UNAVAILABLE("503 Service Temporarily Unavailable", NoticeErrorCodeConstants.SERVICE_UNAVAILABLE);

    HuaweiErrCodeEnum(String code, String noticeCode) {
        this.code = code;
        this.noticeCode = noticeCode;
    }

    private String code;

    private String noticeCode;

    public String getCode() {
        return code;
    }

    public String getNoticeCode() {
        return noticeCode;
    }

    public static String convertErrorCode(String businessErrCode) {
        for (HuaweiErrCodeEnum huaweiErrCodeEnum : HuaweiErrCodeEnum.values()) {
            if (huaweiErrCodeEnum.getCode().equals(businessErrCode)) {
                return huaweiErrCodeEnum.getNoticeCode();
            }
        }
        return businessErrCode;
    }
}
