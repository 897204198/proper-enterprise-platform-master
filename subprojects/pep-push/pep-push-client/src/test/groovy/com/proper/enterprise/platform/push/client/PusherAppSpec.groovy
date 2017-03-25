package com.proper.enterprise.platform.push.client

import spock.lang.Specification

import com.proper.enterprise.platform.push.client.model.PushMessage
import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest

class PusherAppSpec extends Specification{
    def "Using all push methods"() {
        String appkey = "ShengjingOA";
        //        String pushUrl="http://localhost:8080/properpush";
        String pushUrl="https://sjh.sj-hospital.org/properpush";
        String secureKey = "wanchina";
        IPushApiServiceRequest pushApiRequestMock=new PushApiServiceRequestMockService();
        PusherApp app = new PusherApp(pushUrl, appkey, secureKey);
        app.setPushApiRequest(pushApiRequestMock)
        app.setAsync(false);
        PushMessage msg = null;
        msg = new PushMessage();
        msg.setTitle("titletest");
        msg.setContent("titlecontent");
        expect:
        app.pushMessageToOneUser(msg,"sdsyw");
        app.pushMessageToOneDevice(msg,PusherApp.DEVICE_TYPE_ANDROID,"deviceid111");
        app.pushMessageToAllUsers();
        app.pushMessageToAllDevices(msg);
        app.pushMessageToAllDevices(msg,PusherApp.DEVICE_TYPE_ANDROID);

    }
}
