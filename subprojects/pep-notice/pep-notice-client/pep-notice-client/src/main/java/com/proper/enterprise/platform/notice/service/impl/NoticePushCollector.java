package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity;
import com.proper.enterprise.platform.notice.model.TargetModel;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import com.proper.enterprise.platform.notice.service.PushDeviceService;
import com.proper.enterprise.platform.notice.util.NoticeAnalysisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NoticePushCollector implements NoticeCollector {

    private PushDeviceService pushDeviceService;

    @Autowired
    public NoticePushCollector(PushDeviceService pushDeviceService) {
        this.pushDeviceService = pushDeviceService;
    }

    @Override
    public void addNoticeDocument(NoticeDocument noticeDocument) {
    }

    @Override
    public void addNoticeTarget(NoticeDocument noticeDocument, TargetModel targetModel, NoticeType noticeType, User user, NoticeSetDocument
        noticeSetDocument) {
        if (noticeSetDocument.isPush() && noticeType.equals(NoticeType.PUSH)) {
            targetModel.setTo(user.getUsername());
            PushDeviceEntity pushDeviceEntity = pushDeviceService.findDeviceByUserId(user.getId());
            boolean isOk = NoticeAnalysisUtil.isDeviceInfoOk(noticeDocument, user, pushDeviceEntity);
            if (isOk) {
                targetModel.setTargetExtMsg("pushToken", pushDeviceEntity.getPushToken());
                targetModel.setTargetExtMsg("deviceType", pushDeviceEntity.getDevicetype());
                targetModel.setTargetExtMsg("pushMode", pushDeviceEntity.getPushMode());
            }
        }
        noticeDocument.setTarget(targetModel);
    }

}
