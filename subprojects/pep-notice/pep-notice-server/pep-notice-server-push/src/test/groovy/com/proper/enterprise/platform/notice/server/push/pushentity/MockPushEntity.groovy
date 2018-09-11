package com.proper.enterprise.platform.notice.server.push.pushentity

import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType

class MockPushEntity implements ReadOnlyNotice {

    private String id
    private String batchId
    private String appKey
    private String title
    private String content
    private String targetTo
    private Map targetExtMsgMap = new HashMap()
    private Map noticeExtMsgMap = new HashMap()
    private String errorMsg
    private Integer retryCount
    private NoticeType noticeType
    private NoticeStatus status

    NoticeStatus getStatus() {
        return status
    }

    void setStatus(NoticeStatus status) {
        this.status = status
    }

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    String getBatchId() {
        return batchId
    }

    void setBatchId(String batchId) {
        this.batchId = batchId
    }

    String getAppKey() {
        return appKey
    }

    void setAppKey(String appKey) {
        this.appKey = appKey
    }

    String getTitle() {
        return title
    }

    void setTitle(String title) {
        this.title = title
    }

    String getContent() {
        return content
    }

    void setContent(String content) {
        this.content = content
    }

    String getTargetTo() {
        return targetTo
    }

    void setTargetTo(String targetTo) {
        this.targetTo = targetTo
    }

    Map getTargetExtMsgMap() {
        return targetExtMsgMap
    }

    void setTargetExtMsgMap(Map targetExtMsgMap) {
        this.targetExtMsgMap = targetExtMsgMap
    }

    Map getNoticeExtMsgMap() {
        return noticeExtMsgMap
    }

    void setNoticeExtMsgMap(Map noticeExtMsgMap) {
        this.noticeExtMsgMap = noticeExtMsgMap
    }

    String getErrorMsg() {
        return errorMsg
    }

    void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg
    }

    Integer getRetryCount() {
        return retryCount
    }

    void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount
    }

    NoticeType getNoticeType() {
        return noticeType
    }

    void setNoticeType(NoticeType noticeType) {
        this.noticeType = noticeType
    }

}
