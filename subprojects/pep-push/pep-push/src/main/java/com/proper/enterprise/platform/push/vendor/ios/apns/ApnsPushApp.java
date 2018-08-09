package com.proper.enterprise.platform.push.vendor.ios.apns;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.vendor.BasePushApp;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ApnsPushApp extends BasePushApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApnsPushApp.class);

    private String theAppkey;
    private String keyStorePassword;
    private Object keyStoreMeta;
    private boolean envProduct;
    private String topic;

    public String getTheAppkey() {
        return theAppkey;
    }

    public void setTheAppkey(String theAppkey) {
        this.theAppkey = theAppkey;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public Object getKeyStoreMeta() {
        return keyStoreMeta;
    }

    public void setKeyStoreMeta(Object keyStoreMeta) {
        this.keyStoreMeta = keyStoreMeta;
    }

    public boolean isEnvProduct() {
        return envProduct;
    }

    public void setEnvProduct(boolean envProduct) {
        this.envProduct = envProduct;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    private ApnsClient apnsClient;

    /**
     * 推送一条消息
     *
     * @param msg 消息正文
     * @return
     */
    public boolean pushOneMsg(PushMsgEntity msg) {
        LOGGER.info("ios push log step6 content:{},pushId:{},msg:{}", msg.getMcontent(),
            msg.getId(), JSONUtil.toJSONIgnoreException(msg));
        boolean result = false;
        try {
            initApnsClient();

            final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
            payloadBuilder.setAlertBody(msg.getMcontent());
            payloadBuilder.setAlertTitle(msg.getMtitle());
            payloadBuilder.addCustomProperty("customs", msg.getMcustomDatasMap());
            // 默认声音
            payloadBuilder.setSound(ApnsPayloadBuilder.DEFAULT_SOUND_FILENAME);
            // 设置应用的角标数
            Integer badgeNumber = getBadgeNumber(msg);
            if (badgeNumber != null) {
                payloadBuilder.setBadgeNumber(badgeNumber);
            }
            final String payload = payloadBuilder.buildWithDefaultMaximumLength();
            String pushToken = msg.getDevice().getPushToken();
            final String token = TokenUtil.sanitizeTokenString(pushToken);
            msg.setPushToken(pushToken);

            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, topic, payload);
            if (isReallySendMsg()) {
                final Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient
                    .sendNotification(pushNotification);

                final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture
                    .get();
                msg.setMresponse(JSONUtil.toJSON(pushNotificationResponse));
                if (pushNotificationResponse.isAccepted()) {
                    LOGGER.info("Push notitification accepted by APNs gateway. {}", msg.getMcontent());
                    LOGGER.info("success ios push log step6 content:{},pushId:{},msg:{}", msg.getMcontent(),
                        msg.getId(), JSONUtil.toJSONIgnoreException(msg));
                    result = true;
                } else {
                    LOGGER.info(
                        "Notification rejected by the APNs gateway:{},msg:{}",
                        pushNotificationResponse.getRejectionReason(), JSONUtil.toJSONIgnoreException(msg));
                    pushService.onPushTokenInvalid(msg);
                    // 发送消息失败
                    result = false;
                    if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                        LOGGER.info("\t…and the token is invalid as of {}, msg: {}",
                            pushNotificationResponse.getTokenInvalidationTimestamp(), msg.getMcontent());
                    }
                }
            } else {
                LOGGER.info("No need to push notice to real APNs server when 'push_env' set to 'test'! {}", msg.getMcontent());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to send push notification.msg:" + JSONUtil.toJSONIgnoreException(msg), e);
            msg.setMresponse(e + "\t" + e.getMessage());
            result = false;
        }
        return result;
    }

    private void initApnsClient() {
        if (apnsClient != null) {
            return;
        }

        String applePushUrl = envProduct ? ApnsClientBuilder.PRODUCTION_APNS_HOST : ApnsClientBuilder.DEVELOPMENT_APNS_HOST;
        ApnsClientBuilder builder = new ApnsClientBuilder().setApnsServer(applePushUrl);
        try {
            if (keyStoreMeta instanceof File) {
                File p12File = (File) keyStoreMeta;
                builder = builder.setClientCredentials(p12File, keyStorePassword);
            } else if (keyStoreMeta instanceof InputStream) {
                builder = builder.setClientCredentials((InputStream) keyStoreMeta, keyStorePassword);
            }
        } catch (IOException ioe) {
            LOGGER.error("Set APNs client credentials ERROR!", ioe);
        }
        try {
            apnsClient = builder.build();
        } catch (Throwable throwable) {
            LOGGER.error("Build APNs client ERROR!", throwable);
        }
    }

}
