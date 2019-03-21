package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;

import java.util.Map;
import java.util.Set;

public interface NoticeSendService {

    /**
     * 模板方式发送消息
     *
     * @param fromUserId     发起人
     * @param toUserIds      通知接收人ID集合
     * @param code           模板关键字
     * @param templateParams 模板参数
     * @param custom         扩展字段
     */
    void sendNoticeChannel(String fromUserId,
                           Set<String> toUserIds,
                           String code,
                           Map<String, Object> templateParams,
                           Map<String, Object> custom);

    /**
     * 通过指定标题和正文的方式发送消息
     *
     * @param fromUserId 发起人
     * @param toUserIds  通知接收人ID集合
     * @param title      标题
     * @param content    正文
     * @param custom     扩展字段
     * @param catalog    消息设置分类
     * @param noticeType 消息渠道
     */
    void sendNoticeChannel(String fromUserId,
                           Set<String> toUserIds,
                           String title,
                           String content,
                           Map<String, Object> custom,
                           String catalog,
                           NoticeType noticeType);

    /**
     * 发送短信
     *
     * @param phone   手机号
     * @param content 正文
     * @param custom  扩展字段
     */
    void sendNoticeSMS(String phone,
                       String content,
                       Map<String, Object> custom);

    /**
     * 发送邮件
     *
     * @param to            接收账号
     * @param cc            抄送账号
     * @param bcc           暗抄送
     * @param title         标题
     * @param content       正文
     * @param custom        扩展字段
     * @param attachmentIds 附件ID集合
     */
    void sendNoticeEmail(String to,
                         String cc,
                         String bcc,
                         String title,
                         String content,
                         Map<String, Object> custom,
                         String... attachmentIds);
}
