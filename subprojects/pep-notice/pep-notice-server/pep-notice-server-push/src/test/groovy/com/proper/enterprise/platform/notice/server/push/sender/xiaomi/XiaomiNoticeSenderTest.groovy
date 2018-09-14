package com.proper.enterprise.platform.notice.server.push.sender.xiaomi

import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.push.configurator.BasePushConfigApi
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum
import com.proper.enterprise.platform.notice.server.push.mock.MockPushNotice

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletRequest

import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.PUSHTOKEN
import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.APPSECRET
import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.PACKAGENAME
import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.APPKEY

class XiaomiNoticeSenderTest extends AbstractTest{


    @Autowired
    private BasePushConfigApi xiaomiNoticeConfigurator

    @Autowired
    private NoticeSendHandler xiaomiNoticeSender


    @Before
    void initData() {
        String appKey = APPKEY
        Map<String, Object> config = new HashMap<>()
        config.put("appSecret", APPSECRET)
        config.put("pushPackage", PACKAGENAME)
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest()
        mockHttpServletRequest.setParameter("pushChannel", PushChannelEnum.XIAOMI.toString())
        xiaomiNoticeConfigurator.post(appKey, config, mockHttpServletRequest)
    }

    @After
    void destroyData() {
        String appKey = APPKEY
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest()
        mockHttpServletRequest.setParameter("pushChannel", PushChannelEnum.XIAOMI.toString())
        xiaomiNoticeConfigurator.delete(appKey, mockHttpServletRequest)
    }

    @Test
    void testSendMessage() {
        def notice = new MockPushNotice()
        notice.setAppKey(APPKEY)
        notice.setTitle("test")
        notice.setContent("test")
        notice.setTargetTo(PUSHTOKEN)
        xiaomiNoticeSender.send(notice)
    }

    @Test
    void testPassThrough(){
        def notice = new MockPushNotice()
        notice.setAppKey(APPKEY)
        notice.setTitle("test")
        notice.setContent("test")
        notice.setTargetTo(PUSHTOKEN)
        Map<String, Object> customs = new HashMap()
        customs.put("_proper_badge", 5)
        customs.put("_proper_pushtype", "cmd")
        notice.setNoticeExtMsg("customs",  customs)
        xiaomiNoticeSender.send(notice)
    }

}
