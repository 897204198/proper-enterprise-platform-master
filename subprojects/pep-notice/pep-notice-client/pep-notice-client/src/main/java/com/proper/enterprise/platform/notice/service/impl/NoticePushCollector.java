package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeTarget;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import com.proper.enterprise.platform.notice.service.PushDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;


@Service
public class NoticePushCollector implements NoticeCollector {

    @Autowired
    private PushDeviceService pushDeviceService;

    @Value("${pep.push.packageName:unUsed}")
    private String packageName;

    @Override
    public NoticeDocument packageNoticeRequest(String fromUserId,
                                               Set<String> toUserIds,
                                               Map<String, Object> custom,
                                               String title,
                                               String content) {
        NoticeDocument noticeDocument = new NoticeDocument();
        noticeDocument.setTitle(title);
        noticeDocument.setContent(content);
        noticeDocument.setNoticeType(NoticeType.PUSH);
        noticeDocument.setUsers(toUserIds);
        noticeDocument.setNoticeExtMsg(custom);
        noticeDocument.setNoticeExtMsg("packageName", packageName);
        noticeDocument.setNoticeExtMsg("from", fromUserId);
        return noticeDocument;
    }

    @Override
    public NoticeDocument addNoticeTarget(NoticeDocument noticeDocument, User user, NoticeSetDocument
        noticeSetDocument) {
        if (noticeSetDocument.isPush() && noticeDocument.getNoticeType().equals(NoticeType.PUSH)) {
            NoticeTarget targetModel = new NoticeTarget();
            targetModel.setTo(user.getUsername());
            noticeDocument.setTarget(targetModel);
            PushDeviceEntity pushDeviceEntity = pushDeviceService.findDeviceByUserId(user.getId());
            if (pushDeviceEntity != null) {
                targetModel.setTargetExtMsg("pushToken", pushDeviceEntity.getPushToken());
                targetModel.setTargetExtMsg("deviceType", pushDeviceEntity.getDevicetype());
                targetModel.setTargetExtMsg("pushMode", pushDeviceEntity.getPushMode());
            }
        }
        return noticeDocument;
    }

}
