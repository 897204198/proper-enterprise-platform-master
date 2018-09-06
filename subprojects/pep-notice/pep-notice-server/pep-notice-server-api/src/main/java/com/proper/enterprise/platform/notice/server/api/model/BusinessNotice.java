package com.proper.enterprise.platform.notice.server.api.model;

import java.util.Map;

/**
 * 消息接口 业务操作权限
 */
public interface BusinessNotice extends ReadOnlyNotice {

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


}
