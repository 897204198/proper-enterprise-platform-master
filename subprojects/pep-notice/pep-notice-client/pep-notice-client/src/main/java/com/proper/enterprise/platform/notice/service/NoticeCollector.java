package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.model.TargetModel;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;

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
     * @param targetModel       接收人信息
     * @param noticeType        消息类型
     * @param user              用户信息
     * @param noticeSetDocument 用户配置
     */
    void addNoticeTarget(NoticeDocument noticeDocument, TargetModel targetModel, NoticeType noticeType, User user, NoticeSetDocument
        noticeSetDocument);

}
