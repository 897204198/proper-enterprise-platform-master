package com.proper.enterprise.platform.notice.server.push.client.huawei;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private Long tokenExpiredTime;

    public HuaweiNoticeClient(PushConfDocument pushDocument) {
        this.appId = pushDocument.getAppId();
        this.appSecret = pushDocument.getAppSecret();
        this.packageName = pushDocument.getPushPackage();
        refreshAccessTokenAndExpiredTime(pushDocument);
    }

    public Long getTokenExpiredTime() {
        return tokenExpiredTime;
    }

    /**
     * 发送推送
     *
     * @param type   1透传 3消息
     * @param body   消息主体
     * @param notice 消息内容
     * @return result
     * @throws ErrMsgException 自定义异常
     */
    public String send(int type, String body, ReadOnlyNotice notice) {
        //目标设备Token
        JSONArray deviceTokens = new JSONArray();
        deviceTokens.add(notice.getTargetTo());
        PushType pushType = PushType.other;
        String packageName = StringUtil.isNull(this.packageName) ? "c" : this.packageName;
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
        handleAction(action, pushType, ext, packageName);
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
                + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + this.appId + "\"}", "UTF-8");
            resBody = post(postUrl, postBody);
        } catch (IOException e) {
            throw new ErrMsgException("Huawei push post with error");
        }
        return resBody;
    }

    private void refreshAccessTokenAndExpiredTime(PushConfDocument pushDocument) {
        String tokenUrl = "https://login.cloud.huawei.com/oauth2/v2/token";
        try {
            String msgBody = MessageFormat.format(
                "grant_type=client_credentials&client_secret={0}&client_id={1}",
                URLEncoder.encode(pushDocument.getAppSecret(), "UTF-8"), pushDocument.getAppId());
            String response = post(tokenUrl, msgBody);
            LOGGER.debug("Get huawei access token response: {}", response);
            JSONObject obj = JSONObject.parseObject(response);

            tokenExpiredTime = System.currentTimeMillis() + obj.getLong("expires_in") - 5 * 60 * 1000;
            accessToken = obj.getString("access_token");
        } catch (Exception e) {
            LOGGER.error("get accessToken failed with Exception {}", e);
            throw new ErrMsgException("Please check Huawei push config");
        }
    }

    private String post(String postUrl, String postBody) throws IOException {
        ResponseEntity<byte[]> post = HttpClient.post(postUrl, MediaType.APPLICATION_FORM_URLENCODED, postBody);
        return new String(post.getBody(), "UTF-8");
    }

    /**
     * 处理消息点击相关参数
     */
    private void handleAction(JSONObject action, PushType pushType, Map<String, Object> ext, String packageName) {
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
