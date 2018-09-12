package com.proper.enterprise.platform.notice.server.push.sender.huawei;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.client.huawei.HuaweiNoticeClientManagerApi;
import com.proper.enterprise.platform.notice.server.push.sender.AbstractPushSendSupport;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("huaweiNoticeSender")
public class HuaweiNoticeSender extends AbstractPushSendSupport implements NoticeSendHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuaweiNoticeSender.class);

    @Autowired
    private HuaweiNoticeClientManagerApi huaweiNoticeClient;

    @Override
    public void send(ReadOnlyNotice notice) throws NoticeException {
        try {
            doPushNotice(notice);
        } catch (Exception e) {
            LOGGER.error("Error occurs when 1st time push to huawei " + notice.getId(), e);
            try {
                LOGGER.debug("Try to send {} to huawei push 2nd time", notice.getId());
                doPushNotice(notice);
            } catch (Exception e1) {
                // 第二次发送失败才真的发送失败 TODO why?
                LOGGER.error("Error occurs when 2nd time push to huawei " + notice.getId(), e1);
                throw new NoticeException(e1.getMessage());
            }
        }

    }

    @Override
    public void beforeSend(BusinessNotice notice) throws NoticeException {

    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {

    }

    @Override
    public NoticeStatus getStatus(ReadOnlyNotice notice) throws NoticeException {
        return NoticeStatus.SUCCESS;
    }

    private void doPushNotice(ReadOnlyNotice notice) throws NoticeException {
        String resp = "";
        if (isCmdMessage(notice)) {
            resp = doPushCmd(notice, notice.getNoticeExtMsgMap());
            try {
                isSuccess(resp, notice);
            } catch (NoticeException e) {
                throw new NoticeException(e.getMessage());
            }
        } else {
            JSONObject body = new JSONObject();
            //消息标题
            body.put("title", notice.getTitle());
            //消息内容体
            body.put("content", notice.getContent());
            try {
                resp = huaweiNoticeClient.get(notice.getAppKey()).send(3, body.toString(), notice);
            } catch (Exception e) {
                throw new NoticeException(e.getMessage());
            }
            Integer badgeNumber = getBadgeNumber(notice);
            //角标不为空，且当前消息为通知栏消息，则发送一条透传消息，设置应用角标
            if (badgeNumber != null) {
                Map<String, Object> data = new HashMap<>(2);
                //系统消息类型：设置角标
                data.put("_proper_mpage", "badge");
                //应用角标数
                data.put("_proper_badge", badgeNumber);
                doPushCmd(notice, data);
            }
            try {
                isSuccess(resp, notice);
            } catch (NoticeException e) {
                throw new NoticeException(e.getMessage());
            }
        }
    }

    private String doPushCmd(ReadOnlyNotice notice, Map<String, Object> custom) throws NoticeException {
        String s = Json.toJson(custom, JsonFormat.compact());
        try {
            return huaweiNoticeClient.get(notice.getAppKey()).send(1, s, notice);
        } catch (Exception e) {
            throw new NoticeException(e.getMessage());
        }
    }

    private void isSuccess(String res, ReadOnlyNotice notice) throws NoticeException {
        LOGGER.debug("Push to huawei with noticeId:{} has response:{}", notice.getId(), res);
        String key = "msg";
        try {
            JsonNode result = JSONUtil.parse(res, JsonNode.class);
            String successValue = "Success";
            if (!successValue.equals(result.get(key).textValue())) {
                throw new NoticeException("Push to huawei failed with noticeId:" + notice.getId());
            }
        } catch (Exception ex) {
            LOGGER.debug("Error occurs when parsing response of " + notice.getId(), ex);
            throw new NoticeException("Error occurs when parsing response of " + notice.getId(), ex);
        }
    }
}
