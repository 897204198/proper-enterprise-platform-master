package com.proper.enterprise.platform.push.vendor.android.xiaomi

import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.push.entity.PushDeviceEntity
import com.proper.enterprise.platform.push.entity.PushMsgEntity
import spock.lang.Specification

class XiaomiPushAppSpec extends Specification {

    def "testXiaomiPushApp"() {
        def appKey = 'MobileOADev'
        // Token of test device
        def token = 'UWaUVPwMtRaZ/X+vSBt0MUkBD/AfrS9PnhhOikuLkvALDOpZG7+549QF7+u9sDmP'
        def msgTitle = System.getProperty('os.name')
        def msgContent = "$msgTitle ${System.getProperty('os.arch')} push this notification to test Xiaomi push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case"

        XiaomiPushApp pushApp = new XiaomiPushApp()
        pushApp.setTheAppPackage('com.proper.icmp.dev')
        pushApp.setTheAppSecret('RGW+NA+T2ucpEX0a6bxyhA==')

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
        boolean result1 = pushApp.pushOneMsg(msg, 1)

        expect:
        assert result1
    }

}
