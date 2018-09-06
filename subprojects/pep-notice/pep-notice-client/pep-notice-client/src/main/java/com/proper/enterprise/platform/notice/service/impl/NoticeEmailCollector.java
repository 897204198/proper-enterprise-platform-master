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
public class NoticeEmailCollector implements NoticeCollector {


    @Override
    public NoticeDocument packageNoticeRequest(String fromUserId,
                                               Set<String> toUserIds,
                                               Map<String, Object> custom,
                                               String title,
                                               String content) {
        NoticeDocument noticeDocument = new NoticeDocument();
        noticeDocument.setTitle(title);
        noticeDocument.setContent(content);
        noticeDocument.setNoticeType(NoticeType.EMAIL);
        noticeDocument.setUsers(toUserIds);
        noticeDocument.setNoticeExtMsg("from", fromUserId);
        return noticeDocument;
    }

    @Override
    public NoticeDocument addNoticeTarget(NoticeDocument noticeDocument, User user, NoticeSetDocument
        noticeSetDocument) {
        if (noticeSetDocument.isEmail() && noticeDocument.getNoticeType().equals(NoticeType.EMAIL)) {
            NoticeTarget targetModel = new NoticeTarget();
            String target = user.getUsername() + "<" + user.getEmail() + ">";
            targetModel.setTo(target);
            noticeDocument.setTarget(targetModel);
        }
        return noticeDocument;
    }

}
