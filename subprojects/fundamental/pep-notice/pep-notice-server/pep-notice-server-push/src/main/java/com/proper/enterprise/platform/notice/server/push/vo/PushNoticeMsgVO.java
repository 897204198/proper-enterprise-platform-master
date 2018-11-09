package com.proper.enterprise.platform.notice.server.push.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;

public class PushNoticeMsgVO extends BaseVO {

    public interface Single extends VOCommonView {
    }

    /**
     * 应用唯一标识
     */
    @JsonView(Single.class)
    private String appKey;

    /**
     * 同一批消息的批次Id
     */
    @JsonView(Single.class)
    private String batchId;

    /**
     * 消息正文
     */
    @JsonView(Single.class)
    private String content;

    /**
     * 消息状态，是否发送
     */
    @JsonView(Single.class)
    private NoticeStatus status;

    /**
     * 消息发送日期
     */
    @JsonView(Single.class)
    private String sendDate;

    /**
     * 推送渠道
     */
    @JsonView(Single.class)
    private PushChannelEnum pushChannel;

    @JsonView(Single.class)
    private String targetTo;

    /**
     * 异常信息
     */
    @JsonView(Single.class)
    private String errorMsg;

    /**
     * appName
     */
    @JsonView(Single.class)
    private String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NoticeStatus getStatus() {
        return status;
    }

    public void setStatus(NoticeStatus status) {
        this.status = status;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public PushChannelEnum getPushChannel() {
        return pushChannel;
    }

    public void setPushChannel(PushChannelEnum pushChannel) {
        this.pushChannel = pushChannel;
    }

    public String getTargetTo() {
        return targetTo;
    }

    public void setTargetTo(String targetTo) {
        this.targetTo = targetTo;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
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
