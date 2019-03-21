package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.factory.NoticeCollectorFactory;
import com.proper.enterprise.platform.notice.model.TargetModel;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import com.proper.enterprise.platform.notice.service.NoticeSendService;
import com.proper.enterprise.platform.notice.util.NoticeAnalysisUtil;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class NoticeSendServiceImpl extends AbstractNoticeSendServiceImpl implements NoticeSendService {

    @Override
    public NoticeRequest sendNoticeChannel(String fromUserId,
                                           Set<String> toUserIds,
                                           Map<String, NoticeSetDocument> noticeSetMap,
                                           String title,
                                           String content,
                                           Map<String, Object> custom,
                                           NoticeType noticeType,
                                           Collection<? extends User> users) {
        NoticeCollector noticeCollector = NoticeCollectorFactory.create(noticeType);
        NoticeDocument noticeDocument = this.packageNoticeDocument(fromUserId, toUserIds, custom, noticeType, title, content);
        noticeCollector.addNoticeDocument(noticeDocument);
        noticeCollector.addNoticeTarget(noticeDocument, noticeDocument.getNoticeType(), users, noticeSetMap);
        analysis(noticeDocument, users);
        NoticeRequest noticeVO = this.saveNotice(noticeDocument);
        if (!NoticeAnalysisUtil.isNecessaryResult(noticeDocument)) {
            accessNoticeServer(noticeVO);
        }
        return noticeVO;
    }

    @Override
    public void sendNoticeSMS(String phone, String content, Map<String, Object> custom) {
        Set<String> set = new HashSet<>();
        set.add(phone);
        TargetModel targetModel = new TargetModel();
        targetModel.setId(phone);
        targetModel.setName(phone);
        targetModel.setTo(phone);
        NoticeDocument noticeDocument = this.packageNoticeDocument(null, set, custom, NoticeType.SMS, "SMS", content);
        noticeDocument.setTarget(targetModel);
        NoticeRequest noticeVO = this.saveNotice(noticeDocument);
        accessNoticeServer(noticeVO);
    }

    @Override
    public void sendNoticeEmail(String to, String cc, String bcc, String title, String content, Map<String, Object> custom, String... attachmentIds) {
        Set<String> set = new HashSet<>();
        set.add(to);
        TargetModel targetModel = new TargetModel();
        targetModel.setId(to);
        targetModel.setName(to);
        targetModel.setTo(to);
        targetModel.setTargetExtMsg("cc", cc);
        targetModel.setTargetExtMsg("bcc", bcc);
        targetModel.setTargetExtMsg("attachmentIds", attachmentIds);
        NoticeDocument noticeDocument = this.packageNoticeDocument(null, set, custom, NoticeType.EMAIL, title, content);
        noticeDocument.setTarget(targetModel);
        NoticeRequest noticeVO = this.saveNotice(noticeDocument);
        accessNoticeServer(noticeVO);
    }
}
