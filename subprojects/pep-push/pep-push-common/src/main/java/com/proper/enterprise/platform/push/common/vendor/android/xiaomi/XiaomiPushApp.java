package com.proper.enterprise.platform.push.common.vendor.android.xiaomi;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.push.common.db.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.common.vendor.BasePushApp;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Sender;

/**
 * 推送服务类
 * 
 * @author 沈东生
 *
 */
public class XiaomiPushApp extends BasePushApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(XiaomiPushApp.class);
    private String theAppPackage;
    private String theAppSecret;
    Sender client;

    public String getTheAppPackage() {
        return theAppPackage;
    }

    public void setTheAppPackage(String theAppPackage) {
        this.theAppPackage = theAppPackage;
    }

    public String getTheAppSecret() {
        return theAppSecret;
    }

    public void setTheAppSecret(String theAppSecret) {
        this.theAppSecret = theAppSecret;
    }

    Sender getClient() {
        try {
            if (client == null) {
                client = new Sender(theAppSecret);
            }
            return client;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 推送一条消息
     * 
     * @return
     */
    public boolean pushOneMsg(PushMsgEntity msg, int notifyId) {

        boolean result = false;
        Message.Builder msgBuilder = new Message.Builder().title(msg.getMtitle()).description(msg.getMcontent())
                .payload(msg.getMcustoms()).restrictedPackageName(theAppPackage).notifyType(1) // 使用默认提示音提示
                .notifyId(notifyId);
        if (isCmdMessage(msg)) {
            msgBuilder.passThrough(1); // 消息使用透传消息
        } else {
            msgBuilder.passThrough(0); // 消息使用通知栏
        }
        Message toMsg = msgBuilder.build();
        try {

            result = doSendMsg(msg, toMsg);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            try {
                close();
                result = doSendMsg(msg, toMsg);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
                result = false; // 第二次发送失败才真的发送失败
            }
        }
        return result;
    }

    private boolean doSendMsg(PushMsgEntity msg, Message toMsg) throws IOException, ParseException {
        boolean result = false;
        String pushToken = msg.getDevice().getPushToken();
        msg.setPushToken(pushToken);
        // 接口调用
        com.xiaomi.xmpush.server.Result rsp = getClient().send(toMsg, pushToken, 1);
        // 有错误返回
        if (rsp.getErrorCode() == ErrorCode.Success) {
            result = true;
        } else {
            // 先不设设备的状态无效，这里有判断失误的情况。
            // if(rsp.getErrorCode().getValue()==20301){
            // pushService.onPushTokenInvalid(msg);
            // }
            result = false; // 发送消息失败
        }
        LOGGER.info("通知栏消息接口响应：" + rsp);
        msg.setMresponse(JSONUtil.toJSON(rsp));
        return result;
    }

    public void close() {
        if (client != null) {
            client = null;
        }
    }
}
