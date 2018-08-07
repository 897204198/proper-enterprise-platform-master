package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            noticeSetEntity.setEmail(true);
        }
        return noticeSetEntity;
    }

    @Override
    public Map<String, NoticeSetDocument> findMapByNoticeTypeAndUserIds(String noticeType, Set<String> userIds) {
        List<NoticeSetDocument> list = noticeSetRepository.findByNoticeTypeAndUserIdIn(noticeType, userIds);
        Map<String, NoticeSetDocument> result = new HashMap<>(1);
        if (!list.isEmpty()) {
            for (NoticeSetDocument noticeSetDocument : list) {
                result.put(noticeSetDocument.getUserId(), noticeSetDocument);
            }
        }
        return result;
    }

}
