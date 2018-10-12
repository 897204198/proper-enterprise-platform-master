package com.proper.enterprise.platform.notice.server.push.sender.huawei

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.push.constant.HuaweiConstant
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum
import com.proper.enterprise.platform.notice.server.push.mock.MockPushNotice
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class HuaweiNoticeSenderTest extends AbstractTest {

    @Autowired
    private NoticeConfigurator pushNoticeConfigurator

    @Autowired
    private NoticeSendHandler pushNoticeSender

    @Test
    void testHuaweiPush() {
        def config = [:]
        config.put('appSecret', HuaweiConstant.CLIENT_SECRET)
        config.put('pushPackage', HuaweiConstant.PACKAGE_NAME)
        config.put('appId', HuaweiConstant.CLIENT_ID)

        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.HUAWEI.toString())
        pushNoticeConfigurator.post('MobileOADev', config, request)

        def notice = new MockPushNotice()
        notice.setTargetTo(HuaweiConstant.TARGET_TO)
        notice.setAppKey('MobileOADev')
        notice.setTitle(System.getProperty('os.name'))
        notice.setContent("${System.getProperty('os.name')} ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case")

        notice.setTargetExtMsg('pushChannel', 'HUAWEI')
        notice.setNoticeExtMsg('push_type', '')

        def customs = [:]
        customs['_proper_badge'] = 2
        notice.setNoticeExtMsg('customs', customs)

        pushNoticeSender.send(notice)

        notice.setNoticeExtMsg('push_type', 'chat')

        customs['_proper_pushtype'] = 'cmd'
        notice.setNoticeExtMsg('customs', customs)

        pushNoticeSender.send(notice)

        notice.setNoticeExtMsg('uri', 'www.baidu.com')
        pushNoticeSender.send(notice)

        notice.setAppKey("testFail")
        try {
            pushNoticeSender.send(notice)
        } catch (Exception e) {
            e.getMessage().contains("Please check Huawei push config")
        }
    }

    @Test
    void testBeforeSend() {
        def notice = new MockPushNotice()
        notice.setTargetTo(HuaweiConstant.TARGET_TO)
        notice.setTitle(System.getProperty('os.name'))
        notice.setAppKey('MobileOADev')
        notice.setTargetExtMsg('pushChannel', 'HUAWEI')
        try {
            pushNoticeSender.beforeSend(notice)
        } catch (Exception e) {
            e.getMessage().contains("Can't get Huawei push config")
        }
    }

    @Test
    @Ignore
    void testAfterSend() {
        def notice = new MockPushNotice()
        notice.setTargetTo(HuaweiConstant.TARGET_TO)
        notice.setAppKey('MobileOADev')
        notice.setTitle(System.getProperty('os.name'))
        notice.setContent("${System.getProperty('os.name')} ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case")

        notice.setTargetExtMsg('pushChannel', 'HUAWEI')
        pushNoticeSender.afterSend(notice)
    }

    @Test
    void testGetStatus() {
        def notice = new MockPushNotice()
        notice.setTargetTo(HuaweiConstant.TARGET_TO)
        notice.setAppKey('MobileOADev')
        notice.setTitle(System.getProperty('os.name'))
        notice.setContent("${System.getProperty('os.name')} ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case")

        notice.setTargetExtMsg('pushChannel', 'HUAWEI')

        assert NoticeStatus.SUCCESS == pushNoticeSender.getStatus(notice).getNoticeStatus()
    }
}
