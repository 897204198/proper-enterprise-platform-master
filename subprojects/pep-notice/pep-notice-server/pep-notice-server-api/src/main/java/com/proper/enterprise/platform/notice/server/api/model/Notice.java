package com.proper.enterprise.platform.notice.server.api.model;

import com.proper.enterprise.platform.notice.server.api.enums.NoticeStatus;

import java.util.Map;

public interface Notice {

    /**
     * 获取消息Id
     *
     * @return 消息Id
     */
    String getId();

    /**
     * 获取消息批次号
     *
     * @return 消息批次号
     */
    String getBatchId();

    /**
     * 获取应用标识
     *
     * @return 应用标识
     */
    String getAppKey();

    /**
     * 获取消息标题
     *
     * @return 消息标题
     */
    String getTitle();

    /**
     * 获取消息内容
     *
     * @return 消息内容
     */
    String getContent();

    /**
     * 获取消息目标唯一标识
     *
     * @return 消息目标唯一标识
     */
    String getTargetTo();

    /**
     * 获取消息目标扩展信息
     *
     * @return 消息目标扩展信息
     */
    Map<String, Object> getTargetExtMsgMap();

    /**
     * 批量设置消息目标扩展信息
     *
     * @param targetExtMsgMap 消息目标扩展信息
     */
    void setAllTargetExtMsg(Map<String, Object> targetExtMsgMap);

    /**
     * 设置消息目标扩展信息
     *
     * @param key 扩展key
     * @param msg 扩展内容
     */
    void setTargetExtMsg(String key, Object msg);

    /**
     * 获取消息扩展信息
     *
     * @return 消息扩展信息
     */
    Map<String, Object> getNoticeExtMsgMap();

    /**
     * 批量设置消息扩展信息
     *
     * @param noticeExtMsgMap 消息目标扩展信息
     */
    void setAllNoticeExtMsg(Map<String, Object> noticeExtMsgMap);

    /**
     * 设置消息扩展信息
     *
     * @param key 扩展key
     * @param msg 扩展内容
     */
    void setNoticeExtMsg(String key, Object msg);

    /**
     * 获取消息异常
     *
     * @return 消息异常
     */
    String getErrorMsg();

    /**
     * 获取消息重试次数
     *
     * @return 消息重试次数
     */
    Integer getRetryCount();

    /**
     * 获取消息状态
     *
     * @return 消息状态
     */
    NoticeStatus getStatus();
}
