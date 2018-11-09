package com.proper.enterprise.platform.notice.server.api.model;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;

import java.util.Map;

/**
 * 消息接口  只读权限
 */
public interface ReadOnlyNotice extends IBase {

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
     * 获取消息类型
     *
     * @return 消息类型
     */
    NoticeType getNoticeType();

    /**
     * 获取消息状态
     *
     * @return 消息状态
     */
    NoticeStatus getStatus();

    /**
     * 获得异常编码
     *
     * @return 异常编码
     */
    String getErrorCode();
}
