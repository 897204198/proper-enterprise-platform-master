package com.proper.enterprise.platform.notice.server.push.ios

import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.notice.server.push.constant.IOSConstant
import com.proper.enterprise.platform.notice.server.push.sender.AbstractPushSendSupport
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

@Ignore
class IOSPushTest extends Specification {



    @Test
    public void apnsSendTest() throws IOException {
        ApnsClientBuilder builder = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
        Resource[] resources = AntResourceUtil.getResources(IOSConstant.CENT_PATH)
        builder = builder.setClientCredentials(resources[0].inputStream, IOSConstant.PASSWORD)
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
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(TokenUtil.sanitizeTokenString(IOSConstant.TARGET_TO), IOSConstant.TOPIC, payload)
        final Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification)
        final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get()
        expect:
        assert pushNotificationResponse.isAccepted()
        apnsClient.close()
        Thread.sleep(5000)
        final Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture2 = apnsClient.sendNotification(pushNotification)
        final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse2 = sendNotificationFuture.get()
        println()
    }
}
