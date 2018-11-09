package com.proper.enterprise.platform.notice.server.push.sender

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult
import com.proper.enterprise.platform.notice.server.push.constant.HuaweiConstant
import com.proper.enterprise.platform.notice.server.push.mock.MockPushNotice
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class PushNoticeSenderTest extends AbstractJPATest{

    @Autowired
    private NoticeConfigurator pushNoticeConfigurator

    @Autowired
    private NoticeSendHandler pushNoticeSender

    @Test
     void testSend() {
        def config = [:]
        config.put('appSecret', HuaweiConstant.CLIENT_SECRET)
        config.put('pushPackage', HuaweiConstant.PACKAGE_NAME)
        config.put('appId', HuaweiConstant.CLIENT_ID)

        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.HUAWEI.toString())
        pushNoticeConfigurator.post('MobileOADev2', config, request)

        def notice = new MockPushNotice()
        notice.setTargetTo("0867110029070702300001436000CN32")
        notice.setAppKey('MobileOADev2')
        notice.setTitle(System.getProperty('os.name'))
        notice.setContent("${System.getProperty('os.name')} ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case")

        notice.setTargetExtMsg('pushChannel', 'HUAWEI')
        notice.setNoticeExtMsg('push_type', '')

        def customs = [:]
        customs['_proper_badge'] = 2
        notice.setNoticeExtMsg('customs', customs)
        BusinessNoticeResult businessNoticeResult = pushNoticeSender.send(notice)
    }
}
