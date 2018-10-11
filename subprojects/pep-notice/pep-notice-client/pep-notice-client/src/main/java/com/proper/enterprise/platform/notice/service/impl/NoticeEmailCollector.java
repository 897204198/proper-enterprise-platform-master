package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.model.TargetModel;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import org.springframework.stereotype.Service;


@Service
public class NoticeEmailCollector implements NoticeCollector {

    private static final String EMAIL = "email";

    @Override
    public void addNoticeDocument(NoticeDocument noticeDocument) {
    }

    @Override
    public void addNoticeTarget(NoticeDocument noticeDocument, TargetModel targetModel, NoticeType noticeType, User user, NoticeSetDocument
        noticeSetDocument) {
        if (noticeSetDocument.getNoticeChannel().contains(EMAIL) && noticeType.equals(NoticeType.EMAIL)) {
            String target = user.getName() + "<" + user.getEmail() + ">";
            targetModel.setTo(target);
            noticeDocument.setTarget(targetModel);
        }
    }

}
