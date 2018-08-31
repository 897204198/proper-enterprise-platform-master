package com.proper.enterprise.platform.push.vendor.android.xiaomi;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
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

public class XiaomiPushApp extends BasePushApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(XiaomiPushApp.class);
    /**
     * 标题最大长度
     */
    private static final int TITLE_MAX_LENGTH = 15;
    /**
     * 描述最大长度
     */
    private static final int DESCRIPTION_MAX_LENGTH = 127;
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

    boolean pushOneMsg(PushMsgEntity msg, int notifyId) {
        Message.Builder msgBuilder = new Message.Builder()
            .title(StringUtil.abbreviate(msg.getMtitle(), TITLE_MAX_LENGTH))
            .description(StringUtil.abbreviate(msg.getMcontent(), DESCRIPTION_MAX_LENGTH))
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
                // 因为在小米手机的设置角标接口里，角标接口是与通知栏消息绑定在一起的，需要程序自己发送notification,并带上角标数
                newCustomMap.put("_proper_badge_type", "notification");
                newCustomMap.remove("uri");
                msg.setMcustomDatasMap(newCustomMap);
            } else {
                // 消息使用通知栏
                msgBuilder.passThrough(0);
            }
        }

        msgBuilder.payload(msg.getMcustoms());
        Message toMsg = msgBuilder.build();

        boolean result;
        try {
            result = doSendMsg(msg, toMsg);
        } catch (Exception e) {
            LOGGER.error("Error occurs when 1st time push to xiaomi " + msg.getId(), e);
            try {
                close();
                LOGGER.debug("Try to send {} to xiaomi push 2nd time", msg.getId());
                result = doSendMsg(msg, toMsg);
            } catch (Exception ex) {
                LOGGER.error("Error occurs when 2nd time push to xiaomi " + msg.getId(), ex);
                // 第二次发送失败才真的发送失败 TODO why?
                result = false;
            }
        }
        return result;
    }



    private boolean doSendMsg(PushMsgEntity msg, Message toMsg) throws IOException, ParseException {
        String pushToken = msg.getDevice().getPushToken();
        msg.setPushToken(pushToken);
        LOGGER.debug("Prepare to send msg to xiaomi push: {}:{} with token {}", msg.getId(), msg.getMcontent(), pushToken);

        if (!isReallySendMsg()) {
            LOGGER.debug("Pretend to push a notice({}) to Xiaomi push server... and then success.", msg.getId());
            return true;
        }

        com.xiaomi.xmpush.server.Result rsp = getClient().send(toMsg, pushToken, 1);
        msg.setMresponse(JSONUtil.toJSON(rsp));
        LOGGER.debug("Check XiaoMi Message Param title:{} and message:{}", toMsg.getTitle(), JSONUtil.toJSONIgnoreException(toMsg));
        if (rsp.getErrorCode() == ErrorCode.Success) {
            LOGGER.debug("success xiaomi push log step6 pushId:{}, rsp:{}", msg.getId(), JSONUtil.toJSONIgnoreException(rsp));
            return true;
        } else {
            LOGGER.error("error xiaomi push log step6 pushId:{}, rsp:{}", msg.getId(), JSONUtil.toJSONIgnoreException(rsp));
            return false;
        }
    }

    private void close() {
        if (client != null) {
            client = null;
        }
    }

}
