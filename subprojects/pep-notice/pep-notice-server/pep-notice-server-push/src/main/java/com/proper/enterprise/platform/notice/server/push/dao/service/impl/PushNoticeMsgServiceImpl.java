package com.proper.enterprise.platform.notice.server.push.dao.service.impl;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgEntity;
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushNoticeMsgJpaRepository;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgService;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.enums.PushDeviceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushNoticeMsgServiceImpl implements PushNoticeMsgService {

    private PushNoticeMsgJpaRepository pushMsgJpaRepository;

    @Autowired
    public PushNoticeMsgServiceImpl(PushNoticeMsgJpaRepository pushMsgJpaRepository) {
        this.pushMsgJpaRepository = pushMsgJpaRepository;
    }

    @Override
    public void savePushMsg(ReadOnlyNotice readOnlyNotice, PushChannelEnum pushChannel) {
        PushNoticeMsgEntity pushNoticeMsg = new PushNoticeMsgEntity();
        pushNoticeMsg.setAppKey(readOnlyNotice.getAppKey());
        pushNoticeMsg.setContent(readOnlyNotice.getContent());
        pushNoticeMsg.setSendCount(readOnlyNotice.getRetryCount() + 1);
        pushNoticeMsg.setSendDate(DateUtil.toDate(readOnlyNotice.getLastModifyTime(),
            PEPConstants.DEFAULT_DATETIME_FORMAT));
        pushNoticeMsg.setPushChannel(pushChannel);
        pushNoticeMsg.setStatus(readOnlyNotice.getStatus());
        pushNoticeMsg.setTitle(readOnlyNotice.getTitle());
        pushNoticeMsg.setTargetTo(readOnlyNotice.getTargetTo());
        switch (pushChannel) {
            case IOS:
                pushNoticeMsg.setDeviceType(PushDeviceTypeEnum.IOS);
                break;
            case HUAWEI:
            case XIAOMI:
                pushNoticeMsg.setDeviceType(PushDeviceTypeEnum.ANDROID);
                break;
        }
        pushMsgJpaRepository.save(pushNoticeMsg);
    }
}
