package com.proper.enterprise.platform.workflow.plugin.service;

import java.util.Map;
import java.util.Set;

public interface WorkflowAsyncNotice {

    /**
     * 发送异步消息
     *
     * @param code           消息模板
     * @param custom         自定义Map
     * @param userId         发送目标
     * @param templateParams 消息模板
     */
    void sendAsyncNotice(String code,
                         Map<String, Object> custom,
                         String userId,
                         Map<String, Object> templateParams);

    /**EndNoticeImpl
     * 发送异步消息
     *
     * @param code           消息模板
     * @param custom         自定义Map
     * @param userIds        发送目标集合
     * @param templateParams 消息模板
     */
    void sendAsyncNotice(String code,
                         Map<String, Object> custom,
                         Set<String> userIds,
                         Map<String, Object> templateParams);
}
