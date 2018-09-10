package com.proper.enterprise.platform.push.vendor.android.huawei;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.vendor.BasePushApp;
import nsp.NSPClient;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
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

public class HuaweiPushApp extends BasePushApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(HuaweiPushApp.class);
    private String theAppid;
    private String theAppSecret;
    private String packageName;
    private NSPClient client;
    private String accessToken;

    /**
     * 应用级消息下发API
     */
    private static final String API_URL = "https://api.push.hicloud.com/pushsend.do";

    /**
     * accessToken的过期时间
     */
    private long tokenExpiredTime;

    public void setTheAppid(String theAppid) {
        this.theAppid = theAppid;
    }

    public void setTheAppSecret(String theAppSecret) {
        this.theAppSecret = theAppSecret;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    boolean pushOneMsg(PushMsgEntity msg) {
        if (!isReallySendMsg()) {
            LOGGER.debug("Pretend to send cmd push({}) to huawei service... and then success.", msg.getId());
            return true;
        }

        try {
            return doPushMsg(msg);
        } catch (Exception e) {
            LOGGER.error("Error occurs when 1st time push to huawei " + msg.getId(), e);
            try {
                close();
                LOGGER.debug("Try to send {} to huawei push 2nd time", msg.getId());
                return doPushMsg(msg);
            } catch (Exception e1) {
                LOGGER.error("Error occurs when 2nd time push to huawei " + msg.getId(), e1);
                // 第二次发送失败才真的发送失败 TODO why?
                return false;
            }
        }
    }

    private boolean doPushMsg(PushMsgEntity msg) throws IOException {
        LOGGER.debug("huawei push log step6 pushId:{}", msg.getId());
        String pushToken = msg.getDevice().getPushToken();
        msg.setPushToken(pushToken);

        if (isCmdMessage(msg)) {
            String resp = doPushCmd(pushToken, msg.getMcustomDatasMap());
            return handlePushCmdResp(resp, msg);
        }

        JSONObject body = new JSONObject();
        JSONObject ext = new JSONObject();
        //消息标题
        body.put("title", msg.getMtitle());
        //消息内容体
        body.put("content", msg.getMcontent());
        Map<String, Object> custom = msg.getMcustomDatasMap();
        if (custom != null && custom.size() > 0) {
            for (Map.Entry<String, Object> en : custom.entrySet()) {
                ext.put(en.getKey(), en.getValue());
            }
        }
        // 接口调用
        String rsp = sendPushMessage(3, pushToken, body.toString(), ext);
        return handleNotificationRes(rsp, msg);
    }

    private String doPushCmd(String pushToken, Map<String, Object> custom) throws IOException {
        String s = Json.toJson(custom, JsonFormat.compact());
        return sendPushMessage(1, pushToken, s, null);
    }

    /**
     * 发送推送
     *
     * @param type  1透传 3消息
     * @param token token
     * @param body  消息体
     * @param ext   扩展内容
     * @return result
     * @throws IOException io
     */
    private String sendPushMessage(int type, String token, String body, JSONObject ext) throws IOException {
        if (tokenExpiredTime <= System.currentTimeMillis()) {
            refreshAccessTokenAndExpiredTime();
        }
        //PushManager.requestToken为客户端申请token的方法，可以调用多次以防止申请token失败
        //PushToken不支持手动编写，需使用客户端的onToken方法获取
        //目标设备Token
        JSONArray deviceTokens = new JSONArray();
        deviceTokens.add(token);
        PushType pushType = PushType.other;
        if (ext != null) {
            if (StringUtil.isNull(packageName)) {
                packageName = "c";
            }
            String extPushType = ext.getString("push_type");
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
        switch (pushType) {
            case chat:
                //类型1为跳转页面
                action.put("type", 1);
                param.put("intent", ext.get("uri"));
                break;
            case video:
            case other:
            default:
                //类型3为打开APP，其他行为请参考接口文档设置
                action.put("type", 3);
                //定义需要打开的appPkgName
                param.put("appPkgName", packageName);
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
        String postBody = MessageFormat.format(
            "access_token={0}&nsp_svc={1}&nsp_ts={2}&device_token_list={3}&payload={4}&expire_time={5}",
            URLEncoder.encode(accessToken, "UTF-8"),
            URLEncoder.encode("openpush.message.api.send", "UTF-8"),
            URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000), "UTF-8"),
            URLEncoder.encode(deviceTokens.toString(), "UTF-8"),
            URLEncoder.encode(payload.toString(), "UTF-8"),
            URLEncoder.encode(format, "UTF-8"));
        LOGGER.debug("postBody: {}", postBody);
        String postUrl = API_URL + "?nsp_ctx="
            + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + theAppid + "\"}", "UTF-8");
        String resBody = post(postUrl, postBody);
        return resBody;
    }

    /**
     * 获取并刷新下发通知消息的认证 Token 及 Token 过期时间
     *
     * @throws IOException 异常
     */
    private void refreshAccessTokenAndExpiredTime() throws IOException {
        String tokenUrl = "https://login.cloud.huawei.com/oauth2/v2/token";
        String msgBody = MessageFormat.format(
            "grant_type=client_credentials&client_secret={0}&client_id={1}",
            URLEncoder.encode(theAppSecret, "UTF-8"), theAppid);
        String response = post(tokenUrl, msgBody);
        JSONObject obj = null;
        try {
            obj = JSONObject.parseObject(response);
        } catch (Exception e) {
            LOGGER.error("huawei push get params tokenUrl {}, msgBody {}, response {}, exception {}", tokenUrl, msgBody, response, e);
            throw e;
        }
        accessToken = obj.getString("access_token");
        tokenExpiredTime = System.currentTimeMillis() + obj.getLong("expires_in") - 5 * 60 * 1000;
    }

    private String post(String postUrl, String postBody) throws IOException {
        ResponseEntity<byte[]> post = HttpClient.post(postUrl, MediaType.APPLICATION_FORM_URLENCODED, postBody);
        byte[] body = post.getBody();
        if (body == null || body.length <= 0) {
            throw new ErrMsgException("Could NOT get body from " + postUrl);
        }
        return new String(body, PEPConstants.DEFAULT_CHARSET);
    }

    private boolean isSuccess(String msgId, String res) {
        LOGGER.debug("Push to huawei with pushId:{} has response:{}", msgId, res);
        String key = "msg";
        try {
            JsonNode result = JSONUtil.parse(res, JsonNode.class);
            return "Success".equals(result.get(key).textValue());
        } catch (Exception ex) {
            LOGGER.debug("Error occurs when parsing response of " + msgId, ex);
        }
        return false;
    }

    private boolean handlePushCmdResp(String res, PushMsgEntity msg) {
        msg.setMresponse(res);
        return isSuccess(msg.getId(), res);
    }

    private void close() {
        if (client != null) {
            client = null;
        }
    }

    private boolean handleNotificationRes(String res, PushMsgEntity msg) throws IOException {
        Integer badgeNumber = getBadgeNumber(msg);
        //角标不为空，且当前消息为通知栏消息，则发送一条透传消息，设置应用角标
        if (badgeNumber != null) {
            Map<String, Object> data = new HashMap<>(2);
            //系统消息类型：设置角标
            data.put("_proper_mpage", "badge");
            //应用角标数
            data.put("_proper_badge", badgeNumber);
            String badgeResponse = doPushCmd(msg.getPushToken(), data);
            Map<String, Object> mapResponse = new HashMap<>(2);
            mapResponse.put("_proper_badge", badgeResponse);
            mapResponse.put("_proper_response", res);
            msg.setMresponse(JSONUtil.toJSONIgnoreException(mapResponse));
        } else {
            msg.setMresponse(res);
        }
        return isSuccess(msg.getId(), res);
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
