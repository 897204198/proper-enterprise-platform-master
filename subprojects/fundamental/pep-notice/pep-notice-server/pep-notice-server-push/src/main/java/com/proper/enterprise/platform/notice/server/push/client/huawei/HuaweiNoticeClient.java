package com.proper.enterprise.platform.notice.server.push.client.huawei;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.api.util.ThrowableMessageUtil;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.enums.huawei.HuaweiErrCodeEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HuaweiNoticeClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuaweiNoticeClient.class);

    /**
     * 应用级消息下发API
     */
    private static final String API_URL = "https://api.push.hicloud.com/pushsend.do";

    private String appId;
    private String appSecret;
    private String packageName;

    /**
     * accessToken
     */
    private String accessToken;

    /**
     * accessToken的过期时间
     */
    private long tokenExpiredTime;

    public HuaweiNoticeClient(PushConfDocument pushDocument) {
        this.appId = pushDocument.getAppId();
        this.appSecret = pushDocument.getAppSecret();
        this.packageName = pushDocument.getPushPackage();
    }

    public BusinessNoticeResult sendCmdMessage(ReadOnlyNotice notice) {
        return send(1, notice, JSONUtil.toJSONIgnoreException(notice.getNoticeExtMsgMap()));
    }

    public BusinessNoticeResult sendMessage(ReadOnlyNotice notice) {
        Integer badgeNumber = null;
        if (null != notice.getNoticeExtMsgMap()) {
            Map<String, Object> noticeExtMsg = notice.getNoticeExtMsgMap();
            if (null != noticeExtMsg) {
                badgeNumber = (Integer) noticeExtMsg.get("_proper_badge");
            }
        }
        //角标不为空，且当前消息为通知栏消息，则发送一条透传消息，设置应用角标
        if (badgeNumber != null) {
            Map<String, Object> data = new HashMap<>(2);
            //系统消息类型：设置角标
            data.put("_proper_mpage", "badge");
            //应用角标数
            data.put("_proper_badge", badgeNumber);
            BusinessNoticeResult sendResult = send(1, notice, JSONUtil.toJSONIgnoreException(data));
            if (NoticeStatus.FAIL == sendResult.getNoticeStatus()) {
                return sendResult;
            }
        }
        JSONObject body = new JSONObject();
        //消息标题
        body.put("title", notice.getTitle());
        //消息内容体
        body.put("content", notice.getContent());
        return send(3, notice, body.toString());
    }

    /**
     * 发送推送
     *
     * @param type   1透传 3消息
     * @param notice 消息主体
     * @param body   消息内容
     */
    private BusinessNoticeResult send(int type, ReadOnlyNotice notice, String body) {
        // accessToken 如果过期则重新获取 accessToken
        if (tokenExpiredTime <= System.currentTimeMillis()) {
            BusinessNoticeResult refreshResult = refreshAccessTokenAndExpiredTime();
            if (NoticeStatus.FAIL.equals(refreshResult.getNoticeStatus())) {
                return refreshResult;
            }
        }
        // 目标设备Token
        JSONArray deviceTokens = new JSONArray();
        deviceTokens.add(notice.getTargetTo());
        // 获取消息类型(chat, video, other)
        PushType pushType = PushType.other;
        Map customs = notice.getNoticeExtMsgMap();
        if (customs != null) {
            String extPushType = (String) customs.get("push_type");
            if (StringUtil.isEmpty(extPushType)) {
                pushType = PushType.other;
            }
            if (StringUtil.isNotBlank(extPushType)) {
                pushType = PushType.valueOf(extPushType);
            }
            // chat类型推送不包含 uri 会导致推送失败 暂时改为非chat而变成other 等前端提供传递uri方案后改回异常
            String uriKey = "uri";
            if ((PushType.chat).equals(pushType) && StringUtil.isBlank((String) customs.get(uriKey))) {
                pushType = PushType.other;
            }
        }
        // msg 结构体, 包含 type/body/action
        JSONObject msg = new JSONObject();
        // type 1: 透传异步消息 3: 系统通知栏异步消息
        msg.put("type", type);
        //通知栏消息body内容
        msg.put("body", body);
        //消息点击动作, 包含 type(1.自定义intent,2.url跳转,3.打开APP)/param(intent,url,appPkgName)
        JSONObject action = new JSONObject();
        Map<String, Object> params = new HashMap<>(2);
        params.put("intent", customs == null ? null : customs.get("uri"));
        // 获取app端打开包名
        String packageName = StringUtil.isNull(this.packageName) ? "c" : this.packageName;
        params.put("appPkgName", packageName);
        handleAction(action, pushType, params);
        msg.put("action", action);
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
        String resBody = "";
        try {
            Map<String, String> ctx = new HashMap<>(2);
            ctx.put("ver", "1");
            ctx.put("appId", appId);
            postBody = MessageFormat.format(
                "access_token={0}&nsp_ctx={1}&nsp_svc={2}&nsp_ts={3}&device_token_list={4}&payload={5}&expire_time={6}",
                URLEncoder.encode(accessToken, "UTF-8"),
                URLEncoder.encode(JSONUtil.toJSON(ctx), "UTF-8"),
                URLEncoder.encode("openpush.message.api.send", "UTF-8"),
                URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000), "UTF-8"),
                URLEncoder.encode(deviceTokens.toString(), "UTF-8"),
                URLEncoder.encode(payload.toString(), "UTF-8"),
                URLEncoder.encode(format, "UTF-8"));
            LOGGER.debug("postBody: {}", postBody);
            resBody = post(API_URL, postBody);
            return isSuccess(resBody, notice);
        } catch (IOException e) {
            return new BusinessNoticeResult(NoticeStatus.FAIL, e.getMessage(), ThrowableMessageUtil.getStackTrace(e));
        }
    }

    /**
     * 处理消息点击相关参数
     */
    private void handleAction(JSONObject action, PushType pushType, Map<String, Object> params) {
        //消息点击动作参数
        JSONObject param = new JSONObject();
        switch (pushType) {
            case chat:
                //类型1为跳转页面
                action.put("type", 1);
                param.put("intent", params.get("intent"));
                break;
            case video:
            case other:
            default:
                //类型3为打开APP，其他行为请参考接口文档设置
                action.put("type", 3);
                //定义需要打开的appPkgName
                param.put("appPkgName", params.get("appPkgName"));
                break;
        }
        action.put("param", param);
    }

    /**
     * 获取AccessToken
     */
    private BusinessNoticeResult refreshAccessTokenAndExpiredTime() {
        String tokenUrl = "https://login.cloud.huawei.com/oauth2/v2/token";
        try {
            String msgBody = MessageFormat.format(
                "grant_type=client_credentials&client_secret={0}&client_id={1}",
                URLEncoder.encode(appSecret, "UTF-8"), appId);
            String response = post(tokenUrl, msgBody);
            LOGGER.debug("Get huawei access token response: {}", response);
            JSONObject obj = JSONObject.parseObject(response);

            // 设置 access token 过期时间
            tokenExpiredTime = System.currentTimeMillis() + obj.getLong("expires_in") * 1000 - 5 * 60 * 1000;
            accessToken = obj.getString("access_token");
            return new BusinessNoticeResult(NoticeStatus.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("get accessToken failed with Exception {}", e);
            return new BusinessNoticeResult(NoticeStatus.FAIL, "get token  error", ThrowableMessageUtil.getStackTrace(e));
        }
    }

    private String post(String postUrl, String postBody) throws IOException {
        ResponseEntity<byte[]> post = HttpClient.post(postUrl, MediaType.APPLICATION_FORM_URLENCODED, postBody);
        byte[] body = post.getBody();
        if (null == body) {
            return null;
        }
        return new String(body, "UTF-8");
    }

    private BusinessNoticeResult isSuccess(String res, ReadOnlyNotice notice) {
        LOGGER.debug("Push to huawei with noticeId:{} has response:{}", notice.getId(), res);
        String key = "msg";
        try {
            JsonNode result = JSONUtil.parse(res, JsonNode.class);
            String successValue = "Success";
            if (result.get(key) != null && !successValue.equals(result.get(key).textValue())) {
                return new BusinessNoticeResult(NoticeStatus.FAIL,
                    HuaweiErrCodeEnum.convertErrorCode(result.get(key).textValue()), result.get(key).textValue());
            }
            return new BusinessNoticeResult(NoticeStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.debug("Error occurs when parsing response of " + notice.getId(), ex);
            return new BusinessNoticeResult(NoticeStatus.FAIL,
                "Error occurs when parsing response", ThrowableMessageUtil.getStackTrace(ex));
        }
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
