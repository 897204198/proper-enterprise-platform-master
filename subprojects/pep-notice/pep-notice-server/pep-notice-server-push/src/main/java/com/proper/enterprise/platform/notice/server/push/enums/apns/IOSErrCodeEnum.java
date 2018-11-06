package com.proper.enterprise.platform.notice.server.push.enums.apns;

import com.proper.enterprise.platform.notice.server.sdk.constants.NoticeErrorCodeConstants;

public enum IOSErrCodeEnum {

    /**
     * 无效目标
     */
    UNREGISTERED("Unregistered", NoticeErrorCodeConstants.INVALID_TARGET),
    /**
     * 无效Token
     */
    BAD_DEVICE_TOKEN("BadDeviceToken", NoticeErrorCodeConstants.INVALID_TARGET),
    /**
     * token未在包下注册
     */
    DEVICE_TOKEN_NOT_FOR_TOPIC("DeviceTokenNotForTopic", NoticeErrorCodeConstants.INVALID_TARGET);

    IOSErrCodeEnum(String code, String noticeCode) {
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
        for (IOSErrCodeEnum iosErrCodeEnum : IOSErrCodeEnum.values()) {
            if (iosErrCodeEnum.getCode().equals(businessErrCode)) {
                return iosErrCodeEnum.getNoticeCode();
            }
        }
        return businessErrCode;
    }
}
