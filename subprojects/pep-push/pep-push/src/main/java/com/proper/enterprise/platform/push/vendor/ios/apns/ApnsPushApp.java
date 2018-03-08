package com.proper.enterprise.platform.push.vendor.ios.apns;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.vendor.BasePushApp;
import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ApnsClientBuilder;
import com.relayrides.pushy.apns.ClientNotConnectedException;
import com.relayrides.pushy.apns.PushNotificationResponse;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.UnsupportedDataTypeException;
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

    ApnsClient apnsClient;

    /**
     * 推送一条消息
     *
     * @param msg 消息正文
     * @return
     */
    public boolean pushOneMsg(PushMsgEntity msg) {
        LOGGER.info("push log step6 ios pushOneMsg:msg:{}", JSONUtil.toJSONIgnoreException(msg));
        boolean result = false;
        try {
            initApnsClient();

            final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
            payloadBuilder.setAlertBody(msg.getMcontent());
            payloadBuilder.setAlertTitle(msg.getMtitle());
            payloadBuilder.addCustomProperty("customs", msg.getMcustomDatasMap());
            payloadBuilder.setSoundFileName("default"); // 默认声音
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
                    LOGGER.info("Push notitification accepted by APNs gateway.");
                    LOGGER.info("push log step6 ios pushOneMsg success:msg:{}", JSONUtil.toJSONIgnoreException(msg));
                    result = true;
                } else {
                    LOGGER.info(
                        "Notification rejected by the APNs gateway:{},msg:{}",
                        pushNotificationResponse.getRejectionReason(), JSONUtil.toJSONIgnoreException(msg));
                    pushService.onPushTokenInvalid(msg);
                    result = false; // 发送消息失败
                    if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                        LOGGER.info("\t…and the token is invalid as of "
                            + pushNotificationResponse.getTokenInvalidationTimestamp());
                    }

                }
            } else {
                LOGGER.info("Push a notice to APNS server with pushToken:{} ", pushToken);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to send push notification.msg:{}", e, JSONUtil.toJSONIgnoreException(msg));
            if (e.getCause() instanceof ClientNotConnectedException) {
                LOGGER.info("Waiting for client to reconnect…,Reconnected.");
            }
            msg.setMresponse(e + "\t" + e.getMessage());
            result = false;
        }

        return result;

    }

    private void initApnsClient() throws IOException, InterruptedException {
        if (apnsClient != null && !apnsClient.isConnected()) {
            try {
                apnsClient.disconnect();
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            apnsClient = null;
        }
        if (apnsClient == null) {
            if (keyStoreMeta instanceof File) {
                File p12File = (File) keyStoreMeta;
                apnsClient = new ApnsClientBuilder().setClientCredentials(p12File, keyStorePassword).build();
            } else if (keyStoreMeta instanceof InputStream) {
                InputStream inputStream = (InputStream) keyStoreMeta;
                apnsClient = new ApnsClientBuilder().setClientCredentials(inputStream, keyStorePassword).build();
            } else {
                throw new UnsupportedDataTypeException("" + keyStoreMeta);
            }
            String applePushUrl = envProduct ? ApnsClient.PRODUCTION_APNS_HOST : ApnsClient.DEVELOPMENT_APNS_HOST;
            final Future<Void> connectFuture = apnsClient.connect(applePushUrl);
            connectFuture.await();
        }
    }
}
