package com.proper.enterprise.platform.notice.server.push.handler.huawei

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum
import com.proper.enterprise.platform.notice.server.push.pushentity.MockPushEntity
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletRequest

class HuaweiNoticeSenderTest extends AbstractTest {

    @Autowired
    private NoticeConfigurator pushNoticeConfigurator

    @Autowired
    private NoticeSendHandler pushNoticeSender

    @Ignore
    @Test
    void testHuaweiPush() {
        def config = [:]
        config.put('appSecret', 'cb5b99c684477aaa3b6a28b2c7cbe7b2')
        config.put('pushPackage', 'com.proper.icmp.dev')
        config.put('appId', '100213965')

        MockHttpServletRequest request = new MockHttpServletRequest()
        request.setParameter("pushChannel", PushChannelEnum.HUAWEI.toString())
        pushNoticeConfigurator.post('MobileOADev', config, request)

        def notice = new MockPushEntity()
        notice.setTargetTo('0867110029070702300001436000CN01')
        notice.setAppKey('MobileOADev')
        notice.setTitle(System.getProperty('os.name'))
        notice.setContent("${System.getProperty('os.name')} ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case")

        def msgExt = [:]
        msgExt['pushChannel'] = 'HUAWEI'
        notice.setTargetExtMsgMap(msgExt)

        pushNoticeSender.send(notice)
    }
}
