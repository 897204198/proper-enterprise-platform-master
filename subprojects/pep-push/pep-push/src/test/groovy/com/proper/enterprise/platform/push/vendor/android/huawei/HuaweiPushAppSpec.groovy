package com.proper.enterprise.platform.push.vendor.android.huawei

import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.push.entity.PushDeviceEntity
import com.proper.enterprise.platform.push.entity.PushMsgEntity
import spock.lang.Specification

class HuaweiPushAppSpec extends Specification {

    def "testHuaweiPushApp"() {
        def appKey = 'MobileOADev'
        // Token of test device
        def token = '0867110029070702300001436000CN01'
        def msgTitle = System.getProperty('os.name')
        def msgContent = "$msgTitle ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case"

        HuaweiPushApp pushApp = new HuaweiPushApp()
        pushApp.setTheAppid('100213965')
        pushApp.setPackageName('com.proper.icmp.dev')
        pushApp.setTheAppSecret('cb5b99c684477aaa3b6a28b2c7cbe7b2')

        def msg = new PushMsgEntity()
        msg.setAppkey(appKey)
        msg.setMcontent(msgContent)
        msg.setMtitle(msgTitle)
        msg.setPushToken(token)

        def device = new PushDeviceEntity()
        device.setPushToken(token)
        msg.setDevice(device)
        def map = [:]
        map['push_type'] = 'chat'
        msg.setMcustomDatasMap(map)

        System.setProperty('push_env', 'Fire in test')
        ConfCenter.reload()
        boolean result1 = pushApp.pushOneMsg(msg)

        expect:
        assert result1
    }

}
