package com.proper.enterprise.platform.push.client

import com.proper.enterprise.platform.push.client.model.PushMessage
import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest
import spock.lang.Specification
import spock.lang.Unroll

class PusherAppSpec extends Specification{
    
    def "Using all push methods"() {
        String appkey = "ShengjingOA"
        String pushUrl="http://localhost:8080/properpush"
        String secureKey = "wanchina"
        IPushApiServiceRequest pushApiRequestMock=new PushApiServiceRequestMockService();
        PusherApp app = new PusherApp(pushUrl, appkey, secureKey);
        app.setPushApiRequest(pushApiRequestMock)
        app.setAsync(false)
        PushMessage msg = null
        msg = new PushMessage()
        msg.setTitle("titletest")
        msg.setContent("titlecontent")
        expect:
        app.pushMessageToOneUser(msg,"sdsyw")
        app.pushMessageToOneDevice(msg,PusherApp.DEVICE_TYPE_ANDROID,"deviceid111")
        app.pushMessageToAllUsers()
        app.pushMessageToAllDevices(msg)
        app.pushMessageToAllDevices(msg,PusherApp.DEVICE_TYPE_ANDROID)
    }

    @Unroll
    def "Direct msg must with userId"() {
        PusherApp app = new PusherApp('', '', '')

        when:
        app.pushMessageToOneUser(new PushMessage('', msg), userId)

        then:
        thrown(IllegalArgumentException)

        where:
        userId | msg
        '' | ''
        null | ''
    }

}
