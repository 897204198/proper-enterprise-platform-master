package com.proper.enterprise.platform.notice.server.push.handler.xiaomi

import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.push.configurator.BasePushConfigApi
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum
import com.proper.enterprise.platform.notice.server.push.pushentity.MockPushEntity
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mock.web.MockHttpServletRequest

class XiaomiNoticeSenderTest extends AbstractTest{

    @Autowired
    private BasePushConfigApi xiaomiNoticeConfigurator

    @Autowired
    private NoticeSendHandler xiaomiNoticeSender


    @Before
    void initData() {
        String appKey = "MobileOADev"
        Map<String, Object> config = new HashMap<>()
        config.put("appSecret", "RGW+NA+T2ucpEX0a6bxyhA==")
        config.put("pushPackage", "com.proper.icmp.dev")
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest()
        mockHttpServletRequest.setParameter("pushChannel", PushChannelEnum.XIAOMI.toString())
        xiaomiNoticeConfigurator.post(appKey, config, mockHttpServletRequest)
    }

    @After
    void destroyData() {
        String appKey = "MobileOADev"
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest()
        mockHttpServletRequest.setParameter("pushChannel", PushChannelEnum.XIAOMI.toString())
        xiaomiNoticeConfigurator.delete(appKey, mockHttpServletRequest)
    }

    @Test
    void testSendMessage() {
        for (int i = 0; i < 4 ; i++) {
            def notice = new MockPushEntity()
            notice.setAppKey("MobileOADev")
            notice.setTitle("test")
            notice.setContent("test")
            notice.getNoticeExtMsgMap().put("_proper_badge", 5)
            notice.setTargetTo("o4gN1LuTsk6/CM7TKf0tTbj2MIWimxTGxRo8yZFQTJAhNsGlEeZLbMIeYnZ9BshJ")
            xiaomiNoticeSender.send(notice)
        }
    }
}
