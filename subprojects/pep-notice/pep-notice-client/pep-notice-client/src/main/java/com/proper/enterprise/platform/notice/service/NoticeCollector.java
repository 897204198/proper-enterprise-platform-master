package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;

import java.util.Map;
import java.util.Set;

public interface NoticeCollector {

    /**
     * 拼装消息请求
     *
     * @param fromUserId 消息发起人
     * @param toUserIds  消息接收人
     * @param custom     自定义扩展字段
     * @param title      标题
     * @param content    正文
     * @return 消息请求
     */
    NoticeDocument packageNoticeRequest(String fromUserId,
                                        Set<String> toUserIds,
                                        Map<String, Object> custom,
                                        String title,
                                        String content);

    /**
     * 添加一个接收人信息
     *
     * @param noticeDocument 原消息请求
     * @param user     用户信息
     * @param noticeSetDocument 用户配置
     * @return 添加完接收人的消息请求
     */
    NoticeDocument addNoticeTarget(NoticeDocument noticeDocument, User user, NoticeSetDocument noticeSetDocument);

}
