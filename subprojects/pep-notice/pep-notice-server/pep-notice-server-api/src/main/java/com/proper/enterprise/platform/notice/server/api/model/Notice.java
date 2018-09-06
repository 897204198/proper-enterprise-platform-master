package com.proper.enterprise.platform.notice.server.api.model;

import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;

/**
 * 消息接口 所有权限
 */
public interface Notice extends BusinessNotice {

    /**
     * 设置同一批消息的批次Id
     *
     * @param batchId 同一批消息的批次Id
     */
    void setBatchId(String batchId);

    /**
     * 设置消息类型
     *
     * @param noticeType 消息类型
     */
    void setNoticeType(NoticeType noticeType);

    /**
     * 设置应用唯一标识
     *
     * @param appKey 应用唯一标识
     */
    void setAppKey(String appKey);

    /**
     * 设置发送目标唯一标识
     *
     * @param targetTo 发送目标唯一标识
     */
    void setTargetTo(String targetTo);

    /**
     * 设置异常信息
     *
     * @param errorMsg 异常信息
     */
    void setErrorMsg(String errorMsg);

    /**
     * 设置状态
     *
     * @param status 状态
     */
    void setStatus(NoticeStatus status);

    /**
     * 设置重试次数
     *
     * @param retryCount 重试次数
     */
    void setRetryCount(Integer retryCount);
}
