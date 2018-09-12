package com.proper.enterprise.platform.notice.server.push.sender.huawei

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.push.constant.HuaweiConstant
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum
import com.proper.enterprise.platform.notice.server.push.mock.MockPushNotice
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletRequest

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

        MockHttpServletRequest request = new MockHttpServletRequest()
        request.setParameter("pushChannel", PushChannelEnum.HUAWEI.toString())
        pushNoticeConfigurator.post('MobileOADev', config, request)

        def notice = new MockPushNotice()
        notice.setTargetTo(HuaweiConstant.TARGET_TO)
        notice.setAppKey('MobileOADev')
        notice.setTitle(System.getProperty('os.name'))
        notice.setContent("${System.getProperty('os.name')} ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case")

        notice.setTargetExtMsg('pushChannel', 'HUAWEI')
        notice.setNoticeExtMsg('push_type', '')

        pushNoticeSender.send(notice)
    }
}
