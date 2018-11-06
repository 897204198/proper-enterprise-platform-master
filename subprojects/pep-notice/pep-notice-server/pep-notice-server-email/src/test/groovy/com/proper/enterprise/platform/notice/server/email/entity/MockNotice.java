package com.proper.enterprise.platform.notice.server.email.entity;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MockNotice extends BaseVO implements Notice {
    public MockNotice() {
    }

    private String batchId;
    private String appKey;
    private String title;
    private String content;
    private String targetTo;
    private String targetExtMsg;
    private String noticeExtMsg;
    private NoticeType noticeType;
    private NoticeStatus status;
    private Integer retryCount;
    /**
     * 异常编码
     */
    private String errorCode;

    private String errorMsg;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getTargetTo() {
        return targetTo;
    }

    @Override
    public Map<String, Object> getTargetExtMsgMap() {
        if (StringUtil.isEmpty(this.targetExtMsg)) {
            return null;
        }
        try {
            return JSONUtil.parse(targetExtMsg, Map.class);
        } catch (IOException e) {
            throw new ErrMsgException("targetExtMsg deserialization error");
        }
    }

    @Override
    public void setAllTargetExtMsg(Map<String, Object> targetExtMsgMap) {
        if (CollectionUtil.isEmpty(targetExtMsgMap)) {
            return;
        }
        this.targetExtMsg = JSONUtil.toJSONIgnoreException(targetExtMsgMap);
    }

    public void setTargetExtMsg(String targetExtMsg) {
        this.targetExtMsg = targetExtMsg;
    }

    @Override
    public void setTargetExtMsg(String key, Object msg) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        Map<String, Object> targetExtMsgMap = StringUtil.isEmpty(targetExtMsg)
            ? new HashMap<>(16)
            : getTargetExtMsgMap();
        targetExtMsgMap.put(key, msg);
        this.targetExtMsg = JSONUtil.toJSONIgnoreException(targetExtMsgMap);
    }

    @Override
    public Map<String, Object> getNoticeExtMsgMap() {
        if (StringUtil.isEmpty(this.noticeExtMsg)) {
            return null;
        }
        try {
            return JSONUtil.parse(noticeExtMsg, Map.class);
        } catch (IOException e) {
            throw new ErrMsgException("noticeExtMsg deserialization error");
        }
    }

    @Override
    public void setAllNoticeExtMsg(Map<String, Object> noticeExtMsgMap) {
        if (CollectionUtil.isEmpty(noticeExtMsgMap)) {
            return;
        }
        this.noticeExtMsg = JSONUtil.toJSONIgnoreException(noticeExtMsgMap);
    }

    public void setNoticeExtMsg(String noticeExtMsg) {
        this.noticeExtMsg = noticeExtMsg;
    }

    @Override
    public void setNoticeExtMsg(String key, Object msg) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        Map<String, Object> noticeExtMsgMap = StringUtil.isEmpty(this.noticeExtMsg)
            ? new HashMap<>(16)
            : getNoticeExtMsgMap();
        noticeExtMsgMap.put(key, msg);
        this.noticeExtMsg = JSONUtil.toJSONIgnoreException(noticeExtMsgMap);
    }

    public void setTargetTo(String targetTo) {
        this.targetTo = targetTo;
    }

    public String getTargetExtMsg() {
        return targetExtMsg;
    }

    public String getNoticeExtMsg() {
        return noticeExtMsg;
    }

    @Override
    public NoticeStatus getStatus() {
        return status;
    }

    public void setStatus(NoticeStatus status) {
        this.status = status;
    }

    @Override
    public Integer getRetryCount() {
        return retryCount;
    }

    @Override
    public NoticeType getNoticeType() {
        return this.noticeType;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

    @Override
    public void setNoticeType(NoticeType noticeType) {
        this.noticeType = noticeType;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
