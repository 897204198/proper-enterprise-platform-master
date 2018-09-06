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

    private static final String NOTICE_TYPE_BPM = "BPM";

    @Override
    public Map<String, NoticeSetDocument> findMapByCatalogAndUserIds(String catalog, Set<String> userIds) {
        List<NoticeSetDocument> list = noticeSetRepository.findByCatalogAndUserIdIn(catalog, userIds);
        Map<String, NoticeSetDocument> result = new HashMap<>(1);
        if (!list.isEmpty()) {
            for (NoticeSetDocument noticeSetDocument : list) {
                result.put(noticeSetDocument.getUserId(), noticeSetDocument);
            }
        }
        NoticeSetDocument defaultSet = getDefaultByCatalog(catalog);
        Set<String> sameUsers = result.keySet();
        userIds.removeAll(sameUsers);
        for (String userId : userIds) {
            result.put(userId, defaultSet);
        }
        return result;
    }

    private NoticeSetDocument getDefaultByCatalog(String catalog) {
        NoticeSetDocument noticeSetDocument = new NoticeSetDocument(true, false, false);
        if (NOTICE_TYPE_BPM.equals(catalog)) {
            noticeSetDocument = new NoticeSetDocument(true, true, false);
        }
        return noticeSetDocument;
    }

}
