package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeSetServiceImpl implements NoticeSetService {

    @Autowired
    NoticeSetRepository noticeSetRepository;

    @Override
    public NoticeSetDocument findByNoticeTypeAndUserId(String noticeType, String userId) {
        List<NoticeSetDocument> list = noticeSetRepository.findByNoticeTypeAndUserId(noticeType, userId);
        NoticeSetDocument noticeSetEntity;
        if (!list.isEmpty()) {
            noticeSetEntity = list.get(0);
        } else {
            noticeSetEntity = new NoticeSetDocument();
            noticeSetEntity.setPush(true);
        }
        return noticeSetEntity;
    }

}
