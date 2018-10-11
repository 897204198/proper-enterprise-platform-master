package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoticeSetServiceImpl implements NoticeSetService {

    @Autowired
    NoticeSetRepository noticeSetRepository;

    private static final String NOTICE_TYPE_BPM = "BPM";

    @Override
    public List<NoticeSetDocument> findByUserId(String userId) {
        List<DataDic> noticeCatalogs = (List<DataDic>) DataDicUtil.findByCatalog("NOTICE_CATALOG");
        Map<String, NoticeSetDocument> setMap = this.findUserSetMap(userId);
        List<NoticeSetDocument> result = new ArrayList<>();
        for (DataDic temp : noticeCatalogs) {
            NoticeSetDocument noticeSetDocument = setMap.get(temp.getCode());
            if (noticeSetDocument == null) {
                noticeSetDocument = this.getDefaultByCatalog(temp.getCode());
            }
            noticeSetDocument.setCatalog(temp.getCode());
            noticeSetDocument.setName(temp.getName());
            result.add(noticeSetDocument);
        }
        return result;
    }

    @Override
    public NoticeSetDocument save(NoticeSetDocument noticeSetDocument) {
        String id = noticeSetDocument.getId();
        NoticeSetDocument document;
        if (StringUtil.isNotNull(id)) {
            document = noticeSetRepository.findOne(id);
            document.setNoticeChannel(noticeSetDocument.getNoticeChannel());
        } else {
            document = noticeSetDocument;
        }
        document.setUserId(Authentication.getCurrentUserId());
        return noticeSetRepository.save(document);
    }

    private Map<String, NoticeSetDocument> findUserSetMap(String userId) {
        List<NoticeSetDocument> list = noticeSetRepository.findByUserId(userId);
        Map<String, NoticeSetDocument> result = new HashMap<>(0);
        if (!list.isEmpty()) {
            for (NoticeSetDocument noticeSetDocument : list) {
                result.put(noticeSetDocument.getCatalog(), noticeSetDocument);
            }
        }
        return result;
    }

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
        defaultSet.setCatalog(catalog);
        Set<String> calSet = new HashSet<>();
        Set<String> sameUsers = result.keySet();
        calSet.addAll(userIds);
        calSet.removeAll(sameUsers);
        for (String userId : calSet) {
            result.put(userId, defaultSet);
        }
        return result;
    }

    private NoticeSetDocument getDefaultByCatalog(String catalog) {
        NoticeSetDocument noticeSetDocument = new NoticeSetDocument("push");
        if (NOTICE_TYPE_BPM.equals(catalog)) {
            noticeSetDocument = new NoticeSetDocument("push", "email");
        }
        return noticeSetDocument;
    }

}
