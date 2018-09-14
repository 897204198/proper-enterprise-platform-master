package com.proper.enterprise.platform.notice.server.push.sender.huawei;

import com.alibaba.fastjson.JSONObject;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
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

    @Autowired
    private HuaweiNoticeClientManagerApi huaweiNoticeClientManagerApi;

    @Override
    public void send(ReadOnlyNotice notice) throws NoticeException {
        HuaweiNoticeClient huaweiNoticeClient = huaweiNoticeClientManagerApi.get(notice.getAppKey());
        if (isCmdMessage(notice)) {
            Map<String, Object> noticeExtMsg = notice.getNoticeExtMsgMap();
            Map customs = (Map) noticeExtMsg.get(CUSTOM_PROPERTY_KEY);
            huaweiNoticeClient.send(1, notice, JSONUtil.toJSONIgnoreException(customs));
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
        }
    }

    @Override
    public void beforeSend(BusinessNotice notice) throws NoticeException {
        huaweiNoticeClientManagerApi.get(notice.getAppKey());
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {
        super.savePushMsg(notice, PushChannelEnum.HUAWEI);
    }

    @Override
    public NoticeStatus getStatus(ReadOnlyNotice notice) {
        return NoticeStatus.SUCCESS;
    }

}
