package com.proper.enterprise.platform.notice.server.push.sender.huawei;

import com.alibaba.fastjson.JSONObject;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.JSONUtil;
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

import java.util.HashMap;
import java.util.Map;

@Service("huaweiNoticeSender")
public class HuaweiNoticeSender extends AbstractPushSendSupport implements NoticeSendHandler {

    private static final String PUSH_TYPE = "push_type";

    @Autowired
    private HuaweiNoticeClientManagerApi huaweiNoticeClientManagerApi;

    @Override
    public void send(ReadOnlyNotice notice) throws NoticeException {
        HuaweiNoticeClient huaweiNoticeClient = huaweiNoticeClientManagerApi.get(notice.getAppKey());
        if (isCmdMessage(notice)) {
            huaweiNoticeClient.send(1, notice, JSONUtil.toJSONIgnoreException(notice.getNoticeExtMsgMap()));
            super.savePushMsg(null, notice, PushChannelEnum.HUAWEI);
            return;
        }
        JSONObject body = new JSONObject();
        //消息标题
        body.put("title", notice.getTitle());
        //消息内容体
        body.put("content", notice.getContent());
        huaweiNoticeClient.send(3, notice, body.toString());
        Integer badgeNumber = getBadgeNumber(notice);
        //角标不为空，且当前消息为通知栏消息，则发送一条透传消息，设置应用角标
        if (badgeNumber != null) {
            Map<String, Object> data = new HashMap<>(2);
            //系统消息类型：设置角标
            data.put("_proper_mpage", "badge");
            //应用角标数
            data.put("_proper_badge", badgeNumber);
            huaweiNoticeClient.send(1, notice, JSONUtil.toJSONIgnoreException(data));
            super.savePushMsg(null, notice, PushChannelEnum.HUAWEI);
        }
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
