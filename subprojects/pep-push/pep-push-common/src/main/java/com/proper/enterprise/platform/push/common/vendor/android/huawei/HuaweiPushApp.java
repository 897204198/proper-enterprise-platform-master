package com.proper.enterprise.platform.push.common.vendor.android.huawei;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.push.common.db.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.common.vendor.BasePushApp;

import nsp.NSPClient;
import nsp.OAuth2Client;
import nsp.support.common.AccessToken;
import nsp.support.common.NSPException;

/**
 * 推送服务类
 * 
 * @author 沈东生
 *
 */
public class HuaweiPushApp extends BasePushApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(HuaweiPushApp.class);
    private String theAppid;
    private String theAppSecret;
    NSPClient client;

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

    NSPClient getClient() {
        InputStream inStream1 = null;
        InputStream inStream2 = null;
        try {
            if (client == null) {
                String resBasePath = "/conf/push/common/vendor/huawei/keystores/";
                OAuth2Client oauth2Client = new OAuth2Client();
                ClassPathResource res1 = new ClassPathResource(resBasePath + "mykeystorebj.jks");
                inStream1 = res1.getInputStream();
                oauth2Client.initKeyStoreStream(inStream1, "123456");

                AccessToken accessToken = oauth2Client.getAccessToken("client_credentials", theAppid, theAppSecret);

                LOGGER.error("access token :" + accessToken.getAccess_token() + ",expires time[access token 过期时间]:"
                        + accessToken.getExpires_in());
                client = new NSPClient(accessToken.getAccess_token());
                client.initHttpConnections(30, 50); // 设置每个路由的连接数和最大连接数
                ClassPathResource res2 = new ClassPathResource(resBasePath + "mykeystorebj.jks");
                inStream2 = res2.getInputStream();
                client.initKeyStoreStream(inStream2, "123456"); // 如果访问https必须导入证书流和密码

            }
            return client;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        } finally {
            if (inStream1 != null) {
                try {
                    inStream1.close();
                } catch (IOException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            if (inStream2 != null) {
                try {
                    inStream2.close();
                } catch (IOException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * 推送一条消息
     * 
     * @return
     */
    public boolean pushOneMsg(PushMsgEntity msg) {
        boolean result = false;
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

    private boolean doPushMsg(PushMsgEntity msg) throws NSPException {
        boolean result;

        String pushToken = msg.getDevice().getPushToken();
        msg.setPushToken(pushToken);
        if (isCmdMessage(msg)) {
            Map<String, Object> custom = msg.getMcustomDatasMap();
            String rsp = doPushCmd(pushToken, custom);
            LOGGER.debug("单发接口消息响应:" + rsp);
            result = handleCmdRsp(rsp, msg);
            return result;
        } else {
            Map<String, Object> msgBody = new LinkedHashMap<String, Object>();
            msgBody.put("notification_title", msg.getMtitle());
            msgBody.put("notification_content", msg.getMcontent());
            msgBody.put("doings", 1);
            Map<String, Object> custom = msg.getMcustomDatasMap();
            if (custom != null && custom.size() > 0) {
                List<Map<String, Object>> lstCustoms = new ArrayList<Map<String, Object>>();
                for (Map.Entry<String, Object> en : custom.entrySet()) {
                    Map<String, Object> to = new HashMap<String, Object>();
                    to.put(en.getKey(), en.getValue());
                    lstCustoms.add(to);
                }
                msgBody.put("extras", lstCustoms);
            }
            String strAndroidMsgBody = Json.toJson(msgBody, JsonFormat.compact());
            String callMethodName = "openpush.message.psSingleSend"; // 通知欄消息
            // 标识消息类型（缓存机制），必选
            // 由调用端赋值，取值范围（1~100）。当TMID+msgType的值一样时，仅缓存最新的一条消息
            int msgType = 1;
            // 消息是否需要缓存，必选
            // 0：不缓存
            // 1：缓存
            // 缺省值为0
            int cacheMode = 1;

            // 可选
            // 0: 当前用户
            // 1: 主要用户
            // -1: 默认用户
            //
            String userType = "1";
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("deviceToken", pushToken);
            hashMap.put("android", strAndroidMsgBody);
            hashMap.put("cacheMode", cacheMode);
            hashMap.put("msgType", msgType);
            hashMap.put("userType", userType);
            // 接口调用
            String rsp = getClient().call(callMethodName, hashMap, String.class);
            LOGGER.info("单发通知栏消息接口响应：" + rsp);
            result = handleNotificationRsp(rsp, msg);
            return result;
        }

    }

    private String doPushCmd(String pushToken, Map<String, Object> custom) throws NSPException {
        String strAndroidMsgBody = Json.toJson(custom, JsonFormat.compact());
        String callMethodName = "openpush.message.single_send"; // 透傳消息
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("deviceToken", pushToken);
        hashMap.put("message", strAndroidMsgBody);
        hashMap.put("priority", 0); // 高优先级
        hashMap.put("cacheMode", 1); // 进行缓存
        // 标识消息类型（缓存机制），必选
        // 由调用端赋值，取值范围（1~100）。当TMID+msgType的值一样时，仅缓存最新的一条消息
        hashMap.put("msgType", 1);
        // 接口调用
        String rsp = getClient().call(callMethodName, hashMap, String.class);
        return rsp;
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

        private String message;

        private int resultcode;

        private String requestID;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getResultcode() {
            return resultcode;
        }

        public void setResultcode(int resultcode) {
            this.resultcode = resultcode;
        }

        public String getRequestID() {
            return requestID;
        }

        public void setRequestID(String requestID) {
            this.requestID = requestID;
        }

    }

    final ObjectMapper mapper = new ObjectMapper();

    private boolean handleNotificationRsp(String rsp, PushMsgEntity msg) {
        boolean rtn = false;
        try {

            Rsp result = mapper.readValue(rsp, Rsp.class);
            if (result.getResultcode() != null && result.getResultcode().intValue() == 0) {
                rtn = true;
            }
            // else {
            // 先不设设备的状态无效，这里有判断失误的情况。
            // if(result.getResult_code()==80200001){
            // pushService.onPushTokenInvalid(msg);
            // }
            // }
            Integer badgeNumber = getBadgeNumber(msg);
            if (badgeNumber != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("mpage", "properpush_badge");
                data.put("_proper_badge", badgeNumber);
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
            PushRet result = mapper.readValue(rsp, PushRet.class);
            if (result.getResultcode() == 0) {
                return true;
            }
            // else {
            // 先不设设备的状态无效，这里有判断失误的情况。
            // if(result.getResult_code()==80200001){
            // pushService.onPushTokenInvalid(msg);
            // }
            // }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return false;
    }

    public void close() {
        if (client != null) {
            client = null;
        }
    }
}
