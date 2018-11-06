package com.proper.enterprise.platform.notice.server.push.enums.xiaomi;

import com.proper.enterprise.platform.notice.server.sdk.constants.NoticeErrorCodeConstants;

public enum XiaomiErrCodeEnum {

    /**
     * 无效目标
     */
    INVALID_TARGET("No valid targets!", NoticeErrorCodeConstants.INVALID_TARGET);

    XiaomiErrCodeEnum(String code, String noticeCode) {
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
        for (XiaomiErrCodeEnum xiaomiErrCode : XiaomiErrCodeEnum.values()) {
            if (xiaomiErrCode.getCode().equals(businessErrCode)) {
                return xiaomiErrCode.getNoticeCode();
            }
        }
        return businessErrCode;
    }
}
