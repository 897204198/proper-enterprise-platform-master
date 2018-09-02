package com.proper.enterprise.platform.notice.server.api.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.notice.server.api.enums.NoticeStatus;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


public class NoticeVO extends BaseVO {

    public NoticeVO() {
    }

    /**
     * 同一批消息的批次Id
     */
    @NotEmpty(message = "notice.server.param.batchId.cantBeEmpty")
    @Length(message = "notice.server.param.batchId.isTooLong", max = 36)
    private String batchId;
    /**
     * 应用唯一标识
     */
    @NotEmpty(message = "notice.server.param.appKey.cantBeEmpty")
    @Length(message = "notice.server.param.appKey.isTooLong", max = 255)
    private String appKey;
    /**
     * 消息标题
     */
    @Length(message = "notice.server.param.title.isTooLong", max = 36)
    private String title;
    /**
     * 消息内容
     */
    @NotEmpty(message = "notice.server.param.content.cantBeEmpty")
    @Length(message = "notice.server.param.content.isTooLong", max = 2048)
    private String content;

    /**
     * 发送目标的唯一标识
     */
    @NotEmpty(message = "notice.server.param.target.cantBeEmpty")
    @Length(message = "notice.server.param.target.isTooLong", max = 255)
    private String targetTo;

    /**
     * 发送目标扩展信息
     */
    @Length(message = "notice.server.param.targetExtMsg.isTooLong", max = 2048)
    private String targetExtMsg;

    /**
     * 消息扩展信息
     */
    @Length(message = "notice.server.param.noticeExtMsg.isTooLong", max = 2048)
    private String noticeExtMsg;

    /**
     * 消息状态
     */
    @NotNull(message = "notice.server.param.status.cantBeNull")
    private NoticeStatus status;

    /**
     * 已重试次数
     */
    private Integer retryCount;

    /**
     * 异常信息
     */
    @Length(message = "notice.server.param.errMsg.isTooLong", max = 2048)
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

    public void setTargetTo(String targetTo) {
        this.targetTo = targetTo;
    }

    public String getTargetExtMsg() {
        return targetExtMsg;
    }

    public void setTargetExtMsg(String targetExtMsg) {
        this.targetExtMsg = targetExtMsg;
    }

    public String getNoticeExtMsg() {
        return noticeExtMsg;
    }

    public void setNoticeExtMsg(String noticeExtMsg) {
        this.noticeExtMsg = noticeExtMsg;
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
}
