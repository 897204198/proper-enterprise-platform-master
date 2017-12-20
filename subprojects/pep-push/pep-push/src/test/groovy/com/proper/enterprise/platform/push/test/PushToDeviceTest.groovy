package com.proper.enterprise.platform.push.test

import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.push.client.PusherApp
import com.proper.enterprise.platform.push.client.model.PushMessage
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Ignore
import org.junit.Test
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/push/push-users.sql",
    "/com/proper/enterprise/platform/push/push-devices.sql"
])
class PushToDeviceTest extends AbstractTest {

    @Ignore
    @Test
    void test() {
        String appkey = "MobileOA"
        String pushUrl="http://localhost:8080/pep"
        String secureKey = "wanchina"
        PusherApp app = new PusherApp(pushUrl, appkey, secureKey)
        app.setAsync(false)
        PushMessage msg = new PushMessage("push-to-device", DateUtil.getTimestamp())

        app.pushMessageToOneUser(msg,"hex")
//        app.pushMessageToOneDevice(msg,PusherApp.DEVICE_TYPE_ANDROID,"deviceid111")
//        app.pushMessageToAllUsers(msg)
//        app.pushMessageToAllDevices(msg)
//        app.pushMessageToAllDevices(msg,PusherApp.DEVICE_TYPE_ANDROID)
    }

}
