package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;
import java.util.Set;

public interface NoticeSender {

    /**
     * 根据邮箱发送短信
     *
     * @param to            收件人
     * @param cc            抄送人
     * @param bcc           密送人
     * @param title         主题
     * @param content       内容
     * @param custom        扩展属性
     * @param attachmentIds 附件Id集合
     */
    @Async
    void sendNoticeEmail(String to,
                         String cc,
                         String bcc,
                         String title,
                         String content,
                         Map<String, Object> custom,
                         String... attachmentIds);

    /**
     * 根据手机号发送短信
     *
     * @param phone        手机号
     * @param content      短信内容
     * @param custom       扩展属性
     */
    @Async
    void sendNoticeSMS(String phone, String content, Map<String, Object> custom);

    /**
     * 无发送人，单人消息接口
     *
     * @param toUserId         通知接收人ID
     * @param code             模板关键字
     * @param templateParams   模板参数
     * @param custom           扩展字段
     */
    @Async
    void sendNotice(String toUserId, String code, Map<String, Object> templateParams, Map<String, Object> custom);

    /**
     * 无发送人，单人消息接口
     *
     * @param toUserId         通知接收人ID
     * @param code             模板关键字
     * @param custom           扩展字段
     */
    @Async
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
    @Async
    void sendNotice(String toUserId, String title, String content, Map<String, Object> custom, String catalog, NoticeType noticeType);

    /**
     * 无发送人，批量消息接口
     *
     * @param toUserIds        通知接收人ID集合
     * @param code             模板关键字
     * @param templateParams   模板参数
     * @param custom           扩展字段
     */
    @Async
    void sendNotice(Set<String> toUserIds, String code, Map<String, Object> templateParams, Map<String, Object> custom);

    /**
     * 无发送人，批量消息接口
     *
     * @param toUserIds        通知接收人ID集合
     * @param code             模板关键字
     * @param custom           扩展字段
     */
    @Async
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
    @Async
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
    @Async
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
    @Async
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
    @Async
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
    @Async
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
    @Async
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
     * @param catalog     消息设置分类
     * @param noticeType  消息渠道
     */
    @Async
    void sendNotice(String fromUserId, Set<String> toUserIds, String title, String content, Map<String,
        Object> custom, String catalog, NoticeType noticeType);

}
