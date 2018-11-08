package com.proper.enterprise.platform.push.vendor.ios.apns

import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.push.entity.PushDeviceEntity
import com.proper.enterprise.platform.push.entity.PushMsgEntity
import com.proper.enterprise.platform.test.utils.TestResource
import spock.lang.Specification

class ApnsPushAppSpec extends Specification {

    def "testApnsPushApp"() {
        def appKey = 'MobileOADev'
        // Token of test iPhone 5 device
        def token = 'b61485a194f6492d3319966f4ff633d4bbb12a6b712c7a69b4d83a63b58a7f5e'
        def msgTitle = System.getProperty('os.name')
        def msgContent = "$msgTitle ${System.getProperty('os.arch')} push this notification to test APNs push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case"

        ApnsPushApp apnsPushApp = new ApnsPushApp()
        apnsPushApp.setEnvProduct(true)
        apnsPushApp.setKeyStorePassword('1234')
        apnsPushApp.setTopic('com.proper.icmp.dev')
        apnsPushApp.setKeyStoreMeta(TestResource.getFile('conf/push/vendor/apns/icmp_dev_pro.p12'))
        apnsPushApp.setTheAppkey(appKey)

        def msg = new PushMsgEntity()
        msg.setAppkey(appKey)
        msg.setMcontent(msgContent)
        msg.setMtitle(msgTitle)
        msg.setPushToken(token)

        def device = new PushDeviceEntity()
        device.setPushToken(token)
        msg.setDevice(device)

        System.setProperty('push_env', 'Fire in test')
        ConfCenter.reload()
        boolean result1 = apnsPushApp.pushOneMsg(msg)
        boolean result2 = apnsPushApp.pushOneMsg(msg)

        expect:
        assert result1 && result2
    }

}
