package com.proper.enterprise.platform.push.vendor

import com.proper.enterprise.platform.push.vendor.ios.apns.ApnsPushApp
import spock.lang.Specification

class ApnsPushAppSpec extends Specification {

    def "testApnsPushApp"() {
        ApnsPushApp apnsPushApp = new ApnsPushApp()
        apnsPushApp.setTheAppkey("appkey")
        apnsPushApp.setTopic("topic")
        apnsPushApp.setEnvProduct(true)
        expect:
        assert "appkey" == apnsPushApp.getTheAppkey()
        assert "topic" == apnsPushApp.getTopic()
        assert apnsPushApp.isEnvProduct()
    }
}
