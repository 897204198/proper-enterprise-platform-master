package com.proper.enterprise.platform.notice.client;

import com.proper.enterprise.platform.template.vo.TemplateVO;

import java.util.Map;
import java.util.Set;

public interface NoticeSender {

    /**
     * 无发送人，单人消息接口
     *
     * @param toUserId        通知接收人ID
     * @param templateVO    正文
     * @param custom        扩展字段
     */
    void sendNotice(String toUserId, TemplateVO templateVO, Map<String, Object> custom);

    /**
     * 无发送人，批量消息接口
     *
     * @param toUserIds       通知接收人ID集合
     * @param templateVO 正文
     * @param custom        扩展字段
     */
    void sendNotice(Set<String> toUserIds, TemplateVO templateVO, Map<String, Object> custom);

    /**
     * 有发送人，单人消息接口
     *
     * @param fromUserId       发起人
     * @param toUserId        通知接收人ID
     * @param templateVO    正文
     * @param custom        扩展字段
     */
    void sendNotice(String fromUserId, String toUserId, TemplateVO templateVO, Map<String,
        Object> custom);

    /**
     * 有发送人，批量消息接口
     *
     * @param fromUserId       发起人
     * @param toUserIds       通知接收人ID集合
     * @param templateVO 正文
     * @param custom        扩展字段
     */
    void sendNotice(String fromUserId, Set<String> toUserIds, TemplateVO templateVO, Map<String,
        Object> custom);

}
