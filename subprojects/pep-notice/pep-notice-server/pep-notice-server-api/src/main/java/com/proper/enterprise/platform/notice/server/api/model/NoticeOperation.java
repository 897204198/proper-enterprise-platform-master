package com.proper.enterprise.platform.notice.server.api.model;

public interface NoticeOperation extends Notice {

    /**
     * 设置同一批消息的批次Id
     *
     * @param batchId 同一批消息的批次Id
     */
    void setBatchId(String batchId);

    /**
     * 设置应用唯一标识
     *
     * @param appKey 应用唯一标识
     */
    void setAppKey(String appKey);

    /**
     * 设置消息标题
     *
     * @param title 消息标题
     */
    void setTitle(String title);

    /**
     * 设置消息内容
     *
     * @param content 消息内容
     */
    void setContent(String content);

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
}
