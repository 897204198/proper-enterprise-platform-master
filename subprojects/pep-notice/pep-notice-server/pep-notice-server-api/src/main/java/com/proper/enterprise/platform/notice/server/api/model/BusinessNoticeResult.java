package com.proper.enterprise.platform.notice.server.api.model;

import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;

public class BusinessNoticeResult {

    public BusinessNoticeResult(NoticeStatus noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public BusinessNoticeResult(NoticeStatus noticeStatus, String message) {
        this.noticeStatus = noticeStatus;
        this.message = message;
    }

    /**
     * 业务返回状态
     */
    private NoticeStatus noticeStatus;

    /**
     * 业务返回信息
     */
    private String message;

    public NoticeStatus getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(NoticeStatus noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
