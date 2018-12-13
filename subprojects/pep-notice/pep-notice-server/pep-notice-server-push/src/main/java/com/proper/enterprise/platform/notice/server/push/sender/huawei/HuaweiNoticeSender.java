package com.proper.enterprise.platform.notice.server.push.sender.huawei;

import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.api.util.ThrowableMessageUtil;
import com.proper.enterprise.platform.notice.server.push.client.huawei.HuaweiNoticeClient;
import com.proper.enterprise.platform.notice.server.push.client.huawei.HuaweiNoticeClientManagerApi;
import com.proper.enterprise.platform.notice.server.push.convert.PushMsgConvert;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgEntity;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.sender.AbstractPushSendSupport;
import com.proper.enterprise.platform.notice.server.sdk.constants.NoticeErrorCodeConstants;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("huaweiNoticeSender")
public class HuaweiNoticeSender extends AbstractPushSendSupport implements NoticeSendHandler {

    private static final String PUSH_TYPE = "push_type";

    @Autowired
    private HuaweiNoticeClientManagerApi huaweiNoticeClientManagerApi;

    @Override
    public BusinessNoticeResult send(ReadOnlyNotice notice) {
        PushNoticeMsgEntity pushNoticeMsgEntity = PushMsgConvert.convert(notice);
        pushNoticeMsgEntity.setPushChannel(PushChannelEnum.HUAWEI);
        pushNoticeMsgEntity.setDeviceType(PushMsgConvert.convert(PushChannelEnum.HUAWEI));
        pushNoticeMsgEntity.setStatus(NoticeStatus.PENDING);
        String pushId = super.saveOrUpdatePushMsg(pushNoticeMsgEntity).getId();
        try {
            HuaweiNoticeClient huaweiNoticeClient = huaweiNoticeClientManagerApi.get(notice.getAppKey());
            if (isCmdMessage(notice)) {
                BusinessNoticeResult businessNoticeCmdResult = huaweiNoticeClient.sendCmdMessage(notice);
                if (NoticeStatus.FAIL == businessNoticeCmdResult.getNoticeStatus()
                    || NoticeStatus.RETRY == businessNoticeCmdResult.getNoticeStatus()) {
                    super.updateStatus(pushId, businessNoticeCmdResult.getNoticeStatus(),
                        businessNoticeCmdResult.getCode(), businessNoticeCmdResult.getMessage());
                    return businessNoticeCmdResult;
                }
                super.updateStatus(pushId, NoticeStatus.SUCCESS);
                return businessNoticeCmdResult;
            }
            BusinessNoticeResult businessNoticeResult = huaweiNoticeClient.sendMessage(notice);
            if (NoticeStatus.FAIL == businessNoticeResult.getNoticeStatus()
                || NoticeStatus.RETRY == businessNoticeResult.getNoticeStatus()) {
                super.updateStatus(pushId, businessNoticeResult.getNoticeStatus(),
                    businessNoticeResult.getCode(), businessNoticeResult.getMessage());
                return businessNoticeResult;
            }
            super.updateStatus(pushId, NoticeStatus.SUCCESS);
            return businessNoticeResult;
        } catch (Exception e) {
            super.updateStatus(pushId, NoticeStatus.FAIL,
                e.getMessage(), ThrowableMessageUtil.getStackTrace(e));
            return new BusinessNoticeResult(NoticeStatus.FAIL, e.getMessage(), ThrowableMessageUtil.getStackTrace(e));
        }
    }

    @Override
    public BusinessNoticeResult beforeSend(BusinessNotice notice) {
        try {
            huaweiNoticeClientManagerApi.get(notice.getAppKey());
            return new BusinessNoticeResult(NoticeStatus.SUCCESS);
        } catch (Exception e) {
            return new BusinessNoticeResult(NoticeStatus.FAIL,
                NoticeErrorCodeConstants.CHECK_ERROR, e.getMessage());
        }
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {

    }

    @Override
    public BusinessNoticeResult getStatus(ReadOnlyNotice notice) {
        return super.getStatus(notice);
    }

}
