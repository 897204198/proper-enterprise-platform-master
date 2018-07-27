package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.notice.entity.NoticeSetEntity;
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
    public NoticeSetEntity findNoticeSetsByNoticeTypeAndUserId(String noticeType, String userId) {
        List<NoticeSetEntity> list = noticeSetRepository.findByNoticeTypeAndUserId(noticeType, userId);
        NoticeSetEntity noticeSetEntity;
        if (!list.isEmpty()) {
            noticeSetEntity = list.get(0);
        } else {
            noticeSetEntity = new NoticeSetEntity();
            noticeSetEntity.setPush(true);
        }
        return noticeSetEntity;
    }

}
