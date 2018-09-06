package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;

import java.util.Map;
import java.util.Set;

public interface NoticeSender {

    /**
     * 无发送人，单人消息接口
     *
     * @param toUserId         通知接收人ID
     * @param code             模板关键字
     * @param templateParams   模板参数
     * @param custom           扩展字段
     */
    void sendNotice(String toUserId, String code, Map<String, Object> templateParams, Map<String, Object> custom);

    /**
     * 无发送人，单人消息接口
     *
     * @param toUserId         通知接收人ID
     * @param code             模板关键字
     * @param custom           扩展字段
     */
    void sendNotice(String toUserId, String code, Map<String, Object> custom);

    /**
     * 单消息渠道发送接口
     *
     * @param toUserId    通知接收人ID
     * @param title       标题
     * @param content     正文
     * @param custom      扩展字段
     * @param catalog     目录
     * @param noticeType  消息渠道
     */
    void sendNotice(String toUserId, String title, String content, Map<String, Object> custom, String catalog, NoticeType noticeType);

    /**
     * 无发送人，批量消息接口
     *
     * @param toUserIds        通知接收人ID集合
     * @param code             模板关键字
     * @param templateParams   模板参数
     * @param custom           扩展字段
     */
    void sendNotice(Set<String> toUserIds, String code, Map<String, Object> templateParams, Map<String, Object> custom);

    /**
     * 无发送人，批量消息接口
     *
     * @param toUserIds        通知接收人ID集合
     * @param code             模板关键字
     * @param custom           扩展字段
     */
    void sendNotice(Set<String> toUserIds, String code, Map<String, Object> custom);

    /**
     * 单消息渠道发送接口
     *
     * @param toUserIds    通知接收人ID集合
     * @param title       标题
     * @param content     正文
     * @param custom      扩展字段
     * @param catalog     目录
     * @param noticeType  消息渠道
     */
    void sendNotice(Set<String> toUserIds, String title, String content, Map<String, Object> custom, String catalog, NoticeType noticeType);

    /**
     * 有发送人，单人消息接口
     *
     * @param fromUserId       发起人
     * @param toUserId         通知接收人ID
     * @param code             模板关键字
     * @param templateParams   模板参数
     * @param custom           扩展字段
     */
    void sendNotice(String fromUserId, String toUserId, String code, Map<String, Object> templateParams, Map<String,
        Object> custom);

    /**
     * 有发送人，单人消息接口
     *
     * @param fromUserId       发起人
     * @param toUserId         通知接收人ID
     * @param code             模板关键字
     * @param custom           扩展字段
     */
    void sendNotice(String fromUserId, String toUserId, String code, Map<String, Object> custom);

    /**
     * 单消息渠道发送接口
     *
     * @param fromUserId  发起人
     * @param toUserId    通知接收人ID
     * @param title       标题
     * @param content     正文
     * @param custom      扩展字段
     * @param catalog     目录
     * @param noticeType  消息渠道
     */
    void sendNotice(String fromUserId, String toUserId, String title, String content, Map<String, Object> custom,
                    String catalog, NoticeType noticeType);

    /**
     * 有发送人，批量消息接口
     *
     * @param fromUserId       发起人
     * @param toUserIds        通知接收人ID集合
     * @param code             模板关键字
     * @param templateParams   模板参数
     * @param custom           扩展字段
     */
    void sendNotice(String fromUserId, Set<String> toUserIds, String code, Map<String, Object> templateParams, Map<String,
        Object> custom);

    /**
     * 有发送人，批量消息接口
     *
     * @param fromUserId       发起人
     * @param toUserIds        通知接收人ID集合
     * @param code             模板关键字
     * @param custom           扩展字段
     */
    void sendNotice(String fromUserId, Set<String> toUserIds, String code, Map<String,
        Object> custom);

    /**
     * 单消息渠道发送接口
     *
     * @param fromUserId  发起人
     * @param toUserIds   通知接收人ID集合
     * @param title       标题
     * @param content     正文
     * @param custom      扩展字段
     * @param catalog     目录
     * @param noticeType  消息渠道
     */
    void sendNotice(String fromUserId, Set<String> toUserIds, String title, String content, Map<String,
        Object> custom, String catalog, NoticeType noticeType);

}
