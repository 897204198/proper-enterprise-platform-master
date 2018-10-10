package com.proper.enterprise.platform.notice.server.push.sender.huawei;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.client.huawei.HuaweiNoticeClient;
import com.proper.enterprise.platform.notice.server.push.client.huawei.HuaweiNoticeClientManagerApi;
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
    public void send(ReadOnlyNotice notice) throws NoticeException {
        HuaweiNoticeClient huaweiNoticeClient = huaweiNoticeClientManagerApi.get(notice.getAppKey());
        if (isCmdMessage(notice)) {
            huaweiNoticeClient.sendCmdMessage(notice);
            super.savePushMsg(null, notice, PushChannelEnum.HUAWEI);
            return;
        }
        huaweiNoticeClient.sendMessage(notice);
        super.savePushMsg(null, notice, PushChannelEnum.HUAWEI);
    }

    @Override
    public void beforeSend(BusinessNotice notice) throws NoticeException {
        if (null == notice.getNoticeExtMsgMap()) {
            throw new ErrMsgException("huawei config can't missing push_type in noticeExtMap");
        }
        if (null == notice.getNoticeExtMsgMap().get(PUSH_TYPE)) {
            throw new ErrMsgException("huawei config can't missing push_type in noticeExtMap");
        }
        huaweiNoticeClientManagerApi.get(notice.getAppKey());
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {
        super.updatePushMsg(notice, PushChannelEnum.HUAWEI);
    }

    @Override
    public BusinessNoticeResult getStatus(ReadOnlyNotice notice) {
        return new BusinessNoticeResult(NoticeStatus.SUCCESS);
    }

}
