package com.proper.enterprise.platform.push.vendor.android.huawei;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.vendor.BasePushApp;
import nsp.NSPClient;
import nsp.support.common.NSPException;
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
import java.util.*;

/**
 * 推送服务类
 *
 * @author 沈东生
 */
public class HuaweiPushApp extends BasePushApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(HuaweiPushApp.class);
    private String theAppid;
    private String theAppSecret;
    private NSPClient client;
    private String accessToken;
    private String apiUrl; //应用级消息下发API
    private long tokenExpiredTime;  //accessToken的过期时间


    public String getTheAppid() {
        return theAppid;
    }

    public void setTheAppid(String theAppid) {
        this.theAppid = theAppid;
    }

    public String getTheAppSecret() {
        return theAppSecret;
    }

    public void setTheAppSecret(String theAppSecret) {
        this.theAppSecret = theAppSecret;
    }

    /**
     * 推送一条消息
     *
     * @return boolean
     */
    boolean pushOneMsg(PushMsgEntity msg) {
        boolean result;
        try {
            result = doPushMsg(msg);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            try {
                close();
                result = doPushMsg(msg);
            } catch (Exception e1) {
                LOGGER.error(e1.getMessage(), e1);
                result = false; // 第二次发送失败才真的发送失败
            }
        }
        return result;
    }

    private boolean doPushMsg(PushMsgEntity msg) throws NSPException, IOException {
        boolean result;
        String pushToken = msg.getDevice().getPushToken();
        msg.setPushToken(pushToken);
        LOGGER.debug("getMcustoms:" + msg.getMcustoms() + ",getMcontent:" + msg.getMcontent());
        if (isCmdMessage(msg)) {
            Map<String, Object> custom = msg.getMcustomDatasMap();
            String rsp = doPushCmd(pushToken, custom);
            LOGGER.debug("Response: {}", rsp);
            result = handleCmdRsp(rsp, msg);
            return result;
        } else {

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
            String rsp;
            if (isReallySendMsg()) {
                // 接口调用
                rsp = sendPushMessage(3, pushToken, body.toString(), ext);
                LOGGER.info("Response: {}", rsp);

            } else {
                getClient();
                LOGGER.info("Push a notice msg to Huawei push server with pushToken:{}", pushToken);
                rsp = "{\"msg\":\"success\",\"requestID\":\"14948813856457737\",\"resultcode\":0}";
            }

            result = handleNotificationRsp(rsp, msg);
            return result;
        }

    }

    private String doPushCmd(String pushToken, Map<String, Object> custom) throws NSPException, IOException {
        String rsp;
        if (isReallySendMsg()) {
            String s = Json.toJson(custom, JsonFormat.compact());
            rsp = sendPushMessage(1, pushToken, s, null);
        } else {
            getClient();
            LOGGER.info("Push a cmd msg to Huawei push server with pushToken:{}", pushToken);
            rsp = "{\"msg\":\"success\",\"requestID\":\"14948199168335342557\",\"resultcode\":0}";
        }
        return rsp;
    }


    private boolean handleNotificationRsp(String rsp, PushMsgEntity msg) {
        boolean rtn = false;
        try {
            PushRet result = JSONUtil.parse(rsp, PushRet.class);
            if ("success".equals(result.getMsg())) {
                rtn = true;
            }
            Integer badgeNumber = getBadgeNumber(msg);
            //角标不为空，且当前消息为通知栏消息，则发送一条透传消息，设置应用角标
            if (badgeNumber != null && !isCmdMessage(msg)) {
                Map<String, Object> data = new HashMap<>();
                data.put("_proper_mpage", "badge"); //系统消息类型：设置角标
                data.put("_proper_badge", badgeNumber); //应用角标数
                String badgeResponse = doPushCmd(msg.getPushToken(), data);
                Map<String, Object> mapResponse = new HashMap<>();
                mapResponse.put("_proper_badge", badgeResponse);
                mapResponse.put("_proper_response", rsp);
                msg.setMresponse(JSONUtil.toJSON(mapResponse));
            } else {
                msg.setMresponse(rsp);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return rtn;
    }

    private boolean handleCmdRsp(String rsp, PushMsgEntity msg) {
        try {
            msg.setMresponse(rsp);
            PushRet result = JSONUtil.parse(rsp, PushRet.class);
            if ("success".equals(result.getMsg())) {
                return true;
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return false;
    }

    private void close() {
        if (client != null) {
            client = null;
        }
    }


    //获取下发通知消息的认证Token
    private void getClient() throws IOException {
        String tokenUrl = "https://login.cloud.huawei.com/oauth2/v2/token";
        apiUrl = "https://api.push.hicloud.com/pushsend.do";
        String msgBody = MessageFormat.format(
            "grant_type=client_credentials&client_secret={0}&client_id={1}",
            URLEncoder.encode(theAppSecret, "UTF-8"), theAppid);
        ResponseEntity<byte[]> post = HttpClient.post(tokenUrl, MediaType.APPLICATION_FORM_URLENCODED, msgBody);
        String response = new String(post.getBody(), "UTF-8");

        JSONObject obj = JSONObject.parseObject(response);
        accessToken = obj.getString("access_token");
        tokenExpiredTime = System.currentTimeMillis() + obj.getLong("expires_in") - 5 * 60 * 1000;
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
            getClient();
        }
        //PushManager.requestToken为客户端申请token的方法，可以调用多次以防止申请token失败
        //PushToken不支持手动编写，需使用客户端的onToken方法获取
        //目标设备Token
        JSONArray deviceTokens = new JSONArray();
        deviceTokens.add(token);
        String packageName = null;
        if (ext != null) {
            packageName = ext.getString("packageName");
            if (StringUtil.isNull(packageName)) {
                packageName = "c";
            }
        }
        JSONObject param = new JSONObject();
        //定义需要打开的appPkgName
        param.put("appPkgName", packageName);
        JSONObject action = new JSONObject();
        //类型3为打开APP，其他行为请参考接口文档设置
        action.put("type", 3);
        //消息点击动作参数
        action.put("param", param);

        JSONObject msg = new JSONObject();
        //3: 通知栏消息，异步透传消息请根据接口文档设置
        msg.put("type", type);
        //消息点击动作
        msg.put("action", action);
        //通知栏消息body内容
        msg.put("body", body);
        //        JSONObject ext = new JSONObject();//扩展信息，含BI消息统计，特定展示风格，消息折叠。
        //        ext.put("biTag", "Trump");//设置消息标签，如果带了这个标签，会在回执中推送给CP用于检测某种类型消息的到达率和状态
        //        ext.put("icon", "http://pic.qiantucdn.com/58pic/12/38/18/13758PIC4GV.jpg");//自定义推送消息在通知栏的图标,value为一个公网可以访问的URL

        //华为PUSH消息总结构体
        JSONObject hps = new JSONObject();
        hps.put("msg", msg);
        hps.put("ext", ext);

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

        String postUrl = apiUrl + "?nsp_ctx="
            + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + theAppid + "\"}", "UTF-8");
        String resBody = post(postUrl, postBody);
        LOGGER.debug(resBody, resBody);
        return resBody;
    }

    public String post(String postUrl, String postBody) throws IOException {
        ResponseEntity<byte[]> post = HttpClient.post(postUrl, MediaType.APPLICATION_FORM_URLENCODED, postBody);
        return new String(post.getBody(), "UTF-8");
    }

    static class Rsp {
        Integer resultcode;
        String requestID;
        String message;

        public Integer getResultcode() {
            return resultcode;
        }

        public void setResultcode(Integer resultcode) {
            this.resultcode = resultcode;
        }

        public String getRequestID() {
            return requestID;
        }

        public void setRequestID(String requestID) {
            this.requestID = requestID;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    static class PushRet {

        public PushRet() {
        }

        private String msg;
        private String requestID;
        private String resultcode;

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getResultcode() {
            return resultcode;
        }

        public void setResultcode(String resultcode) {
            this.resultcode = resultcode;
        }

        public String getRequestID() {
            return requestID;
        }

        public void setRequestID(String requestID) {
            this.requestID = requestID;
        }

        public String getMsg() {
            return msg;
        }

    }


}
