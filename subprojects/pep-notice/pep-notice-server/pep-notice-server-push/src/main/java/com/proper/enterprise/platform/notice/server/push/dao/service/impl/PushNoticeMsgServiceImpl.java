package com.proper.enterprise.platform.notice.server.push.dao.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgEntity;
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushNoticeMsgJpaRepository;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgService;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.enums.PushDeviceTypeEnum;
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgVO;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PushNoticeMsgServiceImpl implements PushNoticeMsgService {

    private PushNoticeMsgJpaRepository pushMsgJpaRepository;

    @Autowired
    public PushNoticeMsgServiceImpl(PushNoticeMsgJpaRepository pushMsgJpaRepository) {
        this.pushMsgJpaRepository = pushMsgJpaRepository;
    }

    @Override
    public void updatePushMsg(ReadOnlyNotice readOnlyNotice, PushChannelEnum pushChannel) {
        if (null == readOnlyNotice.getId()) {
            return;
        }
        PushNoticeMsgEntity pushNoticeMsgEntity = pushMsgJpaRepository.findPushNoticeMsgEntitiesByNoticeId(readOnlyNotice.getId());
        if (null == pushNoticeMsgEntity) {
            return;
        }
        pushNoticeMsgEntity.setAppKey(readOnlyNotice.getAppKey());
        pushNoticeMsgEntity.setContent(readOnlyNotice.getContent());
        pushNoticeMsgEntity.setSendCount((readOnlyNotice.getRetryCount() == null ? 0 : readOnlyNotice.getRetryCount()) + 1);
        pushNoticeMsgEntity.setPushChannel(pushChannel);
        pushNoticeMsgEntity.setStatus(readOnlyNotice.getStatus());
        pushNoticeMsgEntity.setTitle(readOnlyNotice.getTitle());
        pushNoticeMsgEntity.setTargetTo(readOnlyNotice.getTargetTo());
        pushNoticeMsgEntity.setNoticeId(readOnlyNotice.getId());
        pushNoticeMsgEntity.setBatchId(readOnlyNotice.getBatchId());
        pushNoticeMsgEntity.setErrorMsg(readOnlyNotice.getErrorMsg());
        pushMsgJpaRepository.updateForSelective(pushNoticeMsgEntity);
    }

    @Override
    public void savePushMsg(String messageId, ReadOnlyNotice readOnlyNotice, PushChannelEnum pushChannel) {
        PushNoticeMsgEntity pushNoticeMsg = new PushNoticeMsgEntity();
        pushNoticeMsg.setAppKey(readOnlyNotice.getAppKey());
        pushNoticeMsg.setContent(readOnlyNotice.getContent());
        pushNoticeMsg.setSendCount((readOnlyNotice.getRetryCount() == null ? 0 : readOnlyNotice.getRetryCount()) + 1);
        pushNoticeMsg.setPushChannel(pushChannel);
        pushNoticeMsg.setStatus(readOnlyNotice.getStatus());
        pushNoticeMsg.setTitle(readOnlyNotice.getTitle());
        pushNoticeMsg.setTargetTo(readOnlyNotice.getTargetTo());
        pushNoticeMsg.setNoticeId(readOnlyNotice.getId());
        pushNoticeMsg.setBatchId(readOnlyNotice.getBatchId());
        pushNoticeMsg.setErrorMsg(readOnlyNotice.getErrorMsg());
        pushNoticeMsg.setMessageId(messageId);
        switch (pushChannel) {
            case IOS:
                pushNoticeMsg.setDeviceType(PushDeviceTypeEnum.IOS);
                break;
            case HUAWEI:
            case XIAOMI:
                pushNoticeMsg.setDeviceType(PushDeviceTypeEnum.ANDROID);
                break;
            default:
                break;
        }
        pushMsgJpaRepository.save(pushNoticeMsg);
    }

    @Override
    public DataTrunk<PushNoticeMsgVO> findPagination(String content, NoticeStatus status,
                                                     String appKey, PushChannelEnum pushChannel, Pageable pageable) {
        Page<PushNoticeMsgEntity> page = pushMsgJpaRepository.findPagination(content, status, appKey, pushChannel, pageable);
        return convert(page);
    }

    private DataTrunk<PushNoticeMsgVO> convert(Page<PushNoticeMsgEntity> page) {
        DataTrunk<PushNoticeMsgVO> dataTrunk = new DataTrunk<>();
        dataTrunk.setCount(page.getTotalElements());
        List<PushNoticeMsgVO> list = new ArrayList<>();
        for (PushNoticeMsgEntity pushNoticeMsgEntity : page.getContent()) {
            PushNoticeMsgVO pushNoticeMsgVO = BeanUtil.convert(pushNoticeMsgEntity, PushNoticeMsgVO.class);
            pushNoticeMsgVO.setSendDate(pushNoticeMsgEntity.getCreateTime());
            list.add(pushNoticeMsgVO);
        }
        dataTrunk.setData(list);
        return dataTrunk;
    }
}
