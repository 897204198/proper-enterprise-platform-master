package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.model.TargetModel;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;


@Service
public class NoticeEmailCollector implements NoticeCollector {

    private static final String EMAIL = "email";

    @Override
    public void addNoticeDocument(NoticeDocument noticeDocument) {
    }

    @Override
    public void addNoticeTarget(NoticeDocument noticeDocument,
                                NoticeType noticeType,
                                Collection<? extends User> users,
                                Map<String, NoticeSetDocument> noticeSetMap) {
        StringBuilder targets = new StringBuilder();
        for (User user : users) {
            NoticeSetDocument noticeSetDocument = noticeSetMap.get(user.getId());
            if (noticeSetDocument.getNoticeChannel().contains(EMAIL)
                && noticeType.equals(NoticeType.EMAIL)
                && StringUtil.isNotEmpty(user.getEmail())) {
                targets.append(user.getName() + "<" + user.getEmail() + ">");
                targets.append(",");
            }
        }
        if (targets.length() > 0) {
            targets.deleteCharAt(targets.length() - 1);
        }
        TargetModel targetModel = new TargetModel();
        targetModel.setId(targets.toString());
        targetModel.setName(targets.toString());
        targetModel.setTo(targets.toString());
        noticeDocument.setTarget(targetModel);
    }

}
