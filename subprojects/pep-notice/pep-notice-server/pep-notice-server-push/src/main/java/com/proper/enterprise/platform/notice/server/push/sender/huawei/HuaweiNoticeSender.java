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
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.sender.AbstractPushSendSupport;
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
                huaweiNoticeClient.sendCmdMessage(notice);
                super.updateStatus(pushId, NoticeStatus.SUCCESS);
                return new BusinessNoticeResult(NoticeStatus.SUCCESS);
            }
            huaweiNoticeClient.sendMessage(notice);
            super.updateStatus(pushId, NoticeStatus.SUCCESS);
            return new BusinessNoticeResult(NoticeStatus.SUCCESS);
        } catch (Exception e) {
            super.updateStatus(pushId, NoticeStatus.FAIL, ThrowableMessageUtil.getStackTrace(e));
            return new BusinessNoticeResult(NoticeStatus.FAIL, ThrowableMessageUtil.getStackTrace(e));
        }
    }

    @Override
    public BusinessNoticeResult beforeSend(BusinessNotice notice) {
        if (null == notice.getNoticeExtMsgMap()) {
            return new BusinessNoticeResult(NoticeStatus.FAIL, "huawei config can't missing push_type in noticeExtMap");
        }
        if (null == notice.getNoticeExtMsgMap().get(PUSH_TYPE)) {
            return new BusinessNoticeResult(NoticeStatus.FAIL, "huawei config can't missing push_type in noticeExtMap");
        }
        try {
            huaweiNoticeClientManagerApi.get(notice.getAppKey());
            return new BusinessNoticeResult(NoticeStatus.SUCCESS);
        } catch (Exception e) {
            return new BusinessNoticeResult(NoticeStatus.FAIL, e.getMessage());
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
