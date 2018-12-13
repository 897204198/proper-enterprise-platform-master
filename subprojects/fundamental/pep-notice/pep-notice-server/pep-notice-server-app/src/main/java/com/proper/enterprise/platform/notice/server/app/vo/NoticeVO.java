package com.proper.enterprise.platform.notice.server.app.vo;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class NoticeVO extends BaseVO implements Notice {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeVO.class);

    public NoticeVO() {
    }

    /**
     * 同一批消息的批次Id
     */
    @NotEmpty(message = "{notice.server.param.batchId.cantBeEmpty}")
    @Length(message = "{notice.server.param.batchId.isTooLong}", max = 36)
    private String batchId;
    /**
     * 应用唯一标识
     */
    @NotEmpty(message = "{notice.server.param.appKey.cantBeEmpty}")
    @Length(message = "{notice.server.param.appKey.isTooLong}", max = 255)
    private String appKey;
    /**
     * 消息标题
     */
    @Length(message = "{notice.server.param.title.isTooLong}", max = 36)
    private String title;
    /**
     * 消息内容
     */
    @NotEmpty(message = "{notice.server.param.content.cantBeEmpty}")
    @Length(message = "{notice.server.param.content.isTooLong}", max = 2048)
    private String content;

    /**
     * 发送目标的唯一标识
     */
    @NotNull(message = "{notice.server.param.target.cantBeEmpty}")
    @Length(message = "{notice.server.param.target.isTooLong}", max = 2048)
    private String targetTo;

    /**
     * 发送目标扩展信息
     */
    @Length(message = "{notice.server.param.targetExtMsg.isTooLong}", max = 2048)
    private String targetExtMsg;

    /**
     * 消息扩展信息
     */
    @Length(message = "{notice.server.param.noticeExtMsg.isTooLong}", max = 2048)
    private String noticeExtMsg;

    /**
     * 消息状态
     */
    private NoticeStatus status;

    /**
     * 消息类型
     */
    private NoticeType noticeType;

    /**
     * 已重试次数
     */
    private Integer retryCount;

    /**
     * 异常编码
     */
    private String errorCode;

    /**
     * 异常信息
     */
    private String errorMsg;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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
            LOGGER.error("targetExtMsg deserialization error", e);
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
            LOGGER.error("noticeExtMsg deserialization error", e);
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

    public NoticeStatus getStatus() {
        return status;
    }

    public void setStatus(NoticeStatus status) {
        this.status = status;
    }

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
