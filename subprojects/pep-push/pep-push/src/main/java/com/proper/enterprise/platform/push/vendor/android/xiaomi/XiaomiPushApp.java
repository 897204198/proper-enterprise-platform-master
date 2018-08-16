package com.proper.enterprise.platform.push.vendor.android.xiaomi;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.vendor.BasePushApp;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Sender;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 推送服务类
 *
 * @author 沈东生
 */
public class XiaomiPushApp extends BasePushApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(XiaomiPushApp.class);
    Sender client;
    private String theAppPackage;
    private String theAppSecret;

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
        LOGGER.info("xiaomi push log step6 content:{},pushId:{},msg:{}", msg.getMcontent(), msg.getId(),
            JSONUtil.toJSONIgnoreException(msg));

        boolean result = false;

        Message.Builder msgBuilder = new Message.Builder().title(msg.getMtitle()).description(msg.getMcontent())
            .restrictedPackageName(theAppPackage)
            // 使用默认提示音提示
            .notifyType(1)
            .notifyId(notifyId);

        if (isCmdMessage(msg)) {
            // 消息使用透传消息
            msgBuilder.passThrough(1);
        } else {
            Integer badgeNumber = getBadgeNumber(msg);
            // 通知栏消息且有应用角标
            if (badgeNumber != null) {
                Map<String, Object> newCustomMap = new HashMap<>(msg.getMcustomDatasMap());
                // 系统消息类型：设置角标
                newCustomMap.put("_proper_mpage", "badge");
                // 需要手机端自己生成一个notification通知
                //因为在小米手机的设置角标接口里，角标接口是与通知栏消息绑定在一起的，需要程序自己发送notification,并带上角标数
                newCustomMap.put("_proper_badge_type", "notification");
                newCustomMap.remove("uri");
                // 更新mcustoms
                msg.setMcustomDatasMap(newCustomMap);
            } else {
                // 消息使用通知栏
                msgBuilder.passThrough(0);
            }
        }

        String mcustoms = msg.getMcustoms();
        msgBuilder.payload(mcustoms);
        Message toMsg = msgBuilder.build();
        try {

            result = doSendMsg(msg, toMsg);

        } catch (Exception e) {
            LOGGER.error("error xiaomi push log step6 content:{},pushId:{},msg:{}", msg.getMcontent(), msg.getId(),
                JSONUtil.toJSONIgnoreException(msg), e);
            try {
                close();
                result = doSendMsg(msg, toMsg);
            } catch (Exception ex) {
                LOGGER.error("error xiaomi push log step6 content:{},pushId:{},msg:{}", msg.getMcontent(), msg.getId(),
                    JSONUtil.toJSONIgnoreException(msg), ex);
                // 第二次发送失败才真的发送失败
                result = false;
            }
        }
        return result;
    }

    private boolean doSendMsg(PushMsgEntity msg, Message toMsg) throws IOException, ParseException {
        boolean result = false;
        String pushToken = msg.getDevice().getPushToken();
        msg.setPushToken(pushToken);
        // 接口调用
        if (isReallySendMsg()) {
            com.xiaomi.xmpush.server.Result rsp = getClient().send(toMsg, pushToken, 1);
            // 有错误返回
            if (rsp.getErrorCode() == ErrorCode.Success) {
                LOGGER.info("success xiaomi push log step6 content:{},pushId:{},msg:{}", msg.getMcontent(), msg.getId(),
                    JSONUtil.toJSONIgnoreException(msg));
                result = true;
            } else {
                LOGGER.info("error xiaomi push log step6 content:{},pushId:{},msg:{},rsp:{}", msg.getMcontent(), msg.getId(),
                    JSONUtil.toJSONIgnoreException(msg), rsp);
                // 发送消息失败
                result = false;
            }
            LOGGER.info("Response：{}", rsp);
            msg.setMresponse(JSONUtil.toJSON(rsp));
        } else {
            LOGGER.info("Push a notice to Xiaomi push server with pushToken:{} ", pushToken);
        }


        return result;
    }

    public void close() {
        if (client != null) {
            client = null;
        }
    }
}
