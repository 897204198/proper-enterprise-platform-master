package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;

import java.util.Collection;
import java.util.Map;

public interface NoticeCollector {

    /**
     * 补充消息参数
     *
     * @param noticeDocument    消息
     */
    void addNoticeDocument(NoticeDocument noticeDocument);

    /**
     * 补充接收人信息
     *
     * @param noticeDocument    消息
     * @param noticeType        消息类型
     * @param users             用户信息集合
     * @param noticeSetMap      用户配置集合
     */
    void addNoticeTarget(NoticeDocument noticeDocument,
                         NoticeType noticeType,
                         Collection<? extends User> users,
                         Map<String, NoticeSetDocument> noticeSetMap);

}
