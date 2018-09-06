package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeTarget;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;


@Service
public class NoticeSmsCollector implements NoticeCollector {

    @Override
    public NoticeDocument packageNoticeRequest(String fromUserId,
                                               Set<String> toUserIds,
                                               Map<String, Object> custom,
                                               String title,
                                               String content) {
        NoticeDocument noticeDocument = new NoticeDocument();
        noticeDocument.setTitle(title);
        noticeDocument.setContent(content);
        noticeDocument.setNoticeType(NoticeType.SMS);
        noticeDocument.setUsers(toUserIds);
        noticeDocument.setNoticeExtMsg("from", fromUserId);
        return noticeDocument;
    }

    @Override
    public NoticeDocument addNoticeTarget(NoticeDocument noticeDocument, User user, NoticeSetDocument
        noticeSetDocument) {
        if (noticeSetDocument.isSms() && noticeDocument.getNoticeType().equals(NoticeType.SMS)) {
            NoticeTarget targetModel = new NoticeTarget();
            targetModel.setTo(user.getPhone());
            noticeDocument.setTarget(targetModel);
        }
        return noticeDocument;
    }

}
