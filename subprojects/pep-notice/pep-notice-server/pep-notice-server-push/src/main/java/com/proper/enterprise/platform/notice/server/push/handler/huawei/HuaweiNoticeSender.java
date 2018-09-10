package com.proper.enterprise.platform.notice.server.push.handler.huawei;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.configurator.huawei.HuaweiNoticeClient;
import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.handler.AbstractPushSendSupport;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class HuaweiNoticeSender extends AbstractPushSendSupport implements NoticeSendHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuaweiNoticeSender.class);

    /**
     * 应用级消息下发API
     */
    private static final String API_URL = "https://api.push.hicloud.com/pushsend.do";

    private HuaweiNoticeClient huaweiNoticeClient;

    @Override
    public void send(ReadOnlyNotice notice) throws NoticeException {
        if (!isCmdMessage(notice)) {

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
        return null;
    }

    /**
     * 发送推送
     *
     * @param type  1透传 3消息
     * @param notice 消息内容
     * @return result
     * @throws NoticeException 自定义异常
     */
    private String sendPushMessage(int type, ReadOnlyNotice notice) throws NoticeException {
        String accessToken = huaweiNoticeClient.getAccessToken(notice.getAppKey());
        //目标设备Token
        JSONArray deviceTokens = new JSONArray();
        deviceTokens.add(notice.getTargetTo());
        PushType pushType = PushType.other;
        PushConfDocument pushConfDocument = huaweiNoticeClient.getConf(notice.getAppKey());
        String packageName = "c";
        if (StringUtil.isNotNull(pushConfDocument.getPushPackage())) {
            packageName = pushConfDocument.getPushPackage();
        }
        Map<String, Object> ext = notice.getNoticeExtMsgMap();
        if (ext != null) {
            String extPushType = (String) ext.get("push_type");
            try {
                if (StringUtil.isNotBlank(extPushType)) {
                    pushType = PushType.valueOf(extPushType);
                }
            } catch (Exception e) {
                LOGGER.debug("Fallback to default push type of " + extPushType, e);
                pushType = PushType.other;
            }
        }
        JSONObject msg = new JSONObject();
        //3: 通知栏消息，异步透传消息请根据接口文档设置
        msg.put("type", type);
        //消息点击动作
        JSONObject action = new JSONObject();
        //消息点击动作参数
        JSONObject param = new JSONObject();
        //消息体
        String body = "";
        switch (pushType) {
            case chat:
                //类型1为跳转页面
                action.put("type", 1);
                param.put("intent", ext.get("uri"));
                body = Json.toJson(ext, JsonFormat.compact());
                break;
            case video:
            case other:
            default:
                //类型3为打开APP，其他行为请参考接口文档设置
                action.put("type", 3);
                //定义需要打开的appPkgName
                param.put("appPkgName", packageName);
                JSONObject msgbody = new JSONObject();
                msgbody.put("title", notice.getTitle());
                msgbody.put("content", notice.getContent());
                body = msgbody.toString();
                break;
        }
        action.put("param", param);
        msg.put("action", action);
        //通知栏消息body内容
        msg.put("body", body);
        //华为PUSH消息总结构体
        JSONObject hps = new JSONObject();
        hps.put("msg", msg);
        //扩展信息，含BI消息统计，特定展示风格，消息折叠。
        JSONObject extJson = new JSONObject();
        //设置消息标签，如果带了这个标签，会在回执中推送给CP用于检测某种类型消息的到达率和状态
        extJson.put("biTag", "Trump");
        // TODO 自定义推送消息在通知栏的图标可在 extJson 中加入 icon 属性，value 为一个公网可以访问的URL
        hps.put("ext", extJson);

        JSONObject payload = new JSONObject();
        payload.put("hps", hps);

        String format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date(System.currentTimeMillis() + 3600 * 1000));
        String postBody = "";
        String postUrl = "";
        String resBody = "";
        try {
            postBody = MessageFormat.format(
                "access_token={0}&nsp_svc={1}&nsp_ts={2}&device_token_list={3}&payload={4}&expire_time={5}",
                URLEncoder.encode(accessToken, "UTF-8"),
                URLEncoder.encode("openpush.message.api.send", "UTF-8"),
                URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000), "UTF-8"),
                URLEncoder.encode(deviceTokens.toString(), "UTF-8"),
                URLEncoder.encode(payload.toString(), "UTF-8"),
                URLEncoder.encode(format, "UTF-8"));
            LOGGER.debug("postBody: {}", postBody);
            postUrl = API_URL + "?nsp_ctx="
                + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + pushConfDocument.getAppId() + "\"}", "UTF-8");
            resBody = huaweiNoticeClient.post(postUrl, postBody);
        } catch (Exception e) {
            throw new NoticeException("Fallback to default push type of ", e);
        }
        return resBody;
    }

    enum PushType {
        /**
         * chat
         */
        chat,
        /**
         * video
         */
        video,
        /**
         * other
         */
        other
    }

}
