package com.proper.enterprise.platform.notice.client;

import java.util.Map;
import java.util.Set;

public interface NoticeSender {

    /**
     * 无发送人，单人消息接口
     *
     * @param code           文案关键字
     * @param custom         扩展字段
     * @param userId         通知接收人ID
     * @param templateParams 文案参数
     */
    void sendNotice(String code,
                    Map<String, Object> custom,
                    String userId,
                    Map<String, Object> templateParams);

    /**
     * 有发送人，单人消息接口
     *
     * @param code           文案关键字
     * @param custom         扩展字段
     * @param from           发起人
     * @param userId         通知接收人ID
     * @param templateParams 文案参数
     */
    void sendNotice(String code,
                    Map<String, Object> custom,
                    String from,
                    String userId,
                    Map<String, Object> templateParams);

    /**
     * 无发送人，批量消息接口
     *
     * @param code           文案关键字
     * @param custom         扩展字段
     * @param userIds        通知接收人ID集合
     * @param templateParams 文案参数
     */
    void sendNotice(String code,
                    Map<String, Object> custom,
                    Set<String> userIds,
                    Map<String, Object> templateParams);

    /**
     * 有发送人，批量消息接口
     *
     * @param code           文案关键字
     * @param custom         扩展字段
     * @param from           发起人
     * @param userIds        通知接收人ID集合
     * @param templateParams 文案参数
     */
    void sendNotice(String code,
                    Map<String, Object> custom,
                    String from,
                    Set<String> userIds,
                    Map<String, Object> templateParams);

    /**
     * 无发送人，指定正文，单人消息接口
     *
     * @param businessId 业务ID
     * @param noticeType 通知类型
     * @param custom     扩展字段
     * @param userId     通知接收人ID
     * @param title      标题
     * @param content    正文
     */
    void sendNotice(String businessId,
                    String noticeType,
                    Map<String, Object> custom,
                    String userId,
                    String title,
                    String content);

    /**
     * 有发送人，指定正文，单人消息接口
     *
     * @param businessId 业务ID
     * @param noticeType 通知类型
     * @param custom     扩展字段
     * @param from       发起人
     * @param userId     通知接收人ID
     * @param title      标题
     * @param content    正文
     */
    void sendNotice(String businessId,
                    String noticeType,
                    Map<String, Object> custom,
                    String from,
                    String userId,
                    String title,
                    String content);

    /**
     * 无发送人，指定正文，批量消息接口
     *
     * @param businessId 业务ID
     * @param noticeType 通知类型
     * @param custom     扩展字段
     * @param userIds    通知接收人ID集合
     * @param title      标题
     * @param content    正文
     */
    void sendNotice(String businessId,
                    String noticeType,
                    Map<String, Object> custom,
                    Set<String> userIds,
                    String title,
                    String content);

    /**
     * 有发送人，指定正文，批量消息接口
     *
     * @param businessId 业务ID
     * @param noticeType 通知类型
     * @param custom     扩展字段
     * @param from       发起人
     * @param userIds    通知接收人ID集合
     * @param title      标题
     * @param content    正文
     */
    void sendNotice(String businessId,
                    String noticeType,
                    Map<String, Object> custom,
                    String from,
                    Set<String> userIds,
                    String title,
                    String content);

}
