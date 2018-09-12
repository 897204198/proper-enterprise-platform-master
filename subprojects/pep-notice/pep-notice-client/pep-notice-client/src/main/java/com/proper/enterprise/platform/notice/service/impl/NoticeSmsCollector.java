package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.model.TargetModel;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import org.springframework.stereotype.Service;


@Service
public class NoticeSmsCollector implements NoticeCollector {

    @Override
    public void addNoticeDocument(NoticeDocument noticeDocument) {
    }

    @Override
    public void addNoticeTarget(NoticeDocument noticeDocument, TargetModel targetModel, NoticeType noticeType, User user, NoticeSetDocument
        noticeSetDocument) {
        if (noticeSetDocument.isSms() && noticeType.equals(NoticeType.SMS)) {
            targetModel.setTo(user.getPhone());
        }
    }

}
