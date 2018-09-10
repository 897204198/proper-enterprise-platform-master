package com.proper.enterprise.platform.notice.server.push.ios

import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.notice.server.push.handler.AbstractPushSendSupport
import com.proper.enterprise.platform.test.AbstractTest
import com.turo.pushy.apns.ApnsClient
import com.turo.pushy.apns.ApnsClientBuilder
import com.turo.pushy.apns.PushNotificationResponse
import com.turo.pushy.apns.util.ApnsPayloadBuilder
import com.turo.pushy.apns.util.SimpleApnsPushNotification
import com.turo.pushy.apns.util.TokenUtil
import io.netty.util.concurrent.Future
import org.junit.Ignore
import org.junit.Test
import org.springframework.core.io.Resource
import spock.lang.Specification


class IOSPushTest extends Specification {

    public static
    final String CENT_PATH = 'classpath*:com/proper/enterprise/platform/notice/server/push/ios/cert/icmp_dev_pro.p12'

    public static final String PASSWORD = "1234"

    public static final String TOPIC = "com.proper.icmp.dev"

    public static final String TARGET_TO = "7fbdc8b7c74f678e088c267cd57cf7abb2e2d14cd6aeec4231e1ef3d656ed3bc"

    @Test
    public void apnsSendTest() throws IOException {
        ApnsClientBuilder builder = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
        Resource[] resources = AntResourceUtil.getResources(CENT_PATH)
        builder = builder.setClientCredentials(resources[0].inputStream, PASSWORD)
        ApnsClient apnsClient = builder.build()

        ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder()
        //设置内容
        payloadBuilder.setAlertBody("666")
        //设置标题
        payloadBuilder.setAlertTitle("777")
        //设置自定义属性
        Map coustoms = new HashMap()
        coustoms.put("url", "www.baidu.com")
        payloadBuilder.addCustomProperty(AbstractPushSendSupport.CUSTOM_PROPERTY_KEY, coustoms)
        // 默认声音
        payloadBuilder.setSound(ApnsPayloadBuilder.DEFAULT_SOUND_FILENAME)
        // 设置应用的角标数
        Integer badgeNumber = 3
        if (badgeNumber != null) {
            payloadBuilder.setBadgeNumber(badgeNumber)
        }
        String payload = payloadBuilder.buildWithDefaultMaximumLength()
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(TokenUtil.sanitizeTokenString(TARGET_TO), TOPIC, payload)
        final Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification)
        final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get()
        expect:
        assert pushNotificationResponse.isAccepted()
        apnsClient.close()
        final Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture2 = apnsClient.sendNotification(pushNotification)
        final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse2 = sendNotificationFuture.get()
        println()
    }
}
