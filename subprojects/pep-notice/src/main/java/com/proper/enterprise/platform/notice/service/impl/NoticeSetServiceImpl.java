package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.notice.entity.NoticeSetEntity;
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeSetServiceImpl implements NoticeSetService {

    private static final String NOTICE_CHANNEL_PUSH = "PUSH";
    private static final String NOTICE_CHANNEL_EMAIL = "EMAIL";
    private static final String NOTICE_CHANNEL_SMS = "SMS";

    @Autowired
    NoticeSetRepository noticeSetRepository;

    @Override
    public String findNoticeSetsByNoticeTypeAndUserId(String noticeType, String userId) {
        List<NoticeSetEntity> list = noticeSetRepository.findByNoticeTypeAndUserId(noticeType, userId);
        NoticeSetEntity noticeSetEntity;
        if (!list.isEmpty()) {
            noticeSetEntity = list.get(0);
        } else {
            noticeSetEntity = new NoticeSetEntity();
            noticeSetEntity.setPush(true);
        }
        return convertNoticeSetToContent(noticeSetEntity);
    }

    private String convertNoticeSetToContent(NoticeSetEntity noticeSetEntity) {
        StringBuilder content = new StringBuilder();
        if (noticeSetEntity.isPush()) {
            content.append(NOTICE_CHANNEL_PUSH);
        }
        if (noticeSetEntity.isSms()) {
            content.append(NOTICE_CHANNEL_SMS);
        }
        if (noticeSetEntity.isEmail()) {
            content.append(NOTICE_CHANNEL_EMAIL);
        }
        return content.toString();
    }

}
