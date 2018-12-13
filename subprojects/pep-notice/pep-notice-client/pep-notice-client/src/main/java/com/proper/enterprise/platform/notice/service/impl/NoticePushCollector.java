package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity;
import com.proper.enterprise.platform.notice.model.TargetModel;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import com.proper.enterprise.platform.notice.service.PushDeviceService;
import com.proper.enterprise.platform.notice.util.NoticeAnalysisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;


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

    private static final String PUSH = "push";

    @Override
    public void addNoticeTarget(NoticeDocument noticeDocument,
                                NoticeType noticeType,
                                Collection<? extends User> users,
                                Map<String, NoticeSetDocument> noticeSetMap) {
        for (User user : users) {
            NoticeSetDocument noticeSetDocument = noticeSetMap.get(user.getId());
            TargetModel targetModel = new TargetModel();
            targetModel.setId(user.getId());
            targetModel.setName(user.getName());
            if (noticeSetDocument.getNoticeChannel().contains(PUSH) && noticeType.equals(NoticeType.PUSH)) {
                PushDeviceEntity pushDeviceEntity = pushDeviceService.findDeviceByUserId(user.getId());
                boolean isOk = NoticeAnalysisUtil.isDeviceInfoOk(noticeDocument, user, pushDeviceEntity);
                if (isOk) {
                    targetModel.setTo(pushDeviceEntity.getPushToken());
                    targetModel.setTargetExtMsg("deviceType", pushDeviceEntity.getDeviceType());
                    targetModel.setTargetExtMsg("pushChannel", pushDeviceEntity.getPushMode());
                    noticeDocument.setTarget(targetModel);
                }
            }
        }
    }

}
