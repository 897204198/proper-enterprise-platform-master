package com.proper.enterprise.platform.push.client

import com.proper.enterprise.platform.push.client.model.PushMessage
import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest
import spock.lang.Specification
import spock.lang.Unroll

class PusherAppSpec extends Specification{

    def "Using all push methods"() {
        String appkey = "test"
        String pushUrl="http://localhost:8080/pep"
        String secureKey = "wanchina"
        PusherApp app = new PusherApp(pushUrl, appkey, secureKey)
        IPushApiServiceRequest pushApiRequestMock=new PushApiServiceRequestMockService()
        app.setPushApiRequest(pushApiRequestMock)
        app.setAsync(false)
        PushMessage msg = new PushMessage("titletest", "titlecontent")

        expect:
        app.pushMessageToOneUser(msg,"sdsyw")
        app.pushMessageToOneDevice(msg,PusherApp.DEVICE_TYPE_ANDROID,"deviceid111")
        app.pushMessageToAllUsers(msg)
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
