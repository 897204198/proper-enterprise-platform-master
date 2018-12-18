package com.proper.enterprise.platform.notice.server.push.sender.xiaomi

import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult
import com.proper.enterprise.platform.notice.server.push.configurator.BasePushConfigApi
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum
import com.proper.enterprise.platform.notice.server.push.enums.xiaomi.XiaomiErrCodeEnum
import com.proper.enterprise.platform.notice.server.push.mock.MockPushNotice
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.PUSHTOKEN
import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.APPSECRET
import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.PACKAGENAME

class XiaomiNoticeSenderTest extends AbstractJPATest {


    @Autowired
    private BasePushConfigApi xiaomiNoticeConfigurator

    @Autowired
    private NoticeSendHandler xiaomiNoticeSender

    private static final String APPKEY = "xiaomiTest"

    @Before
    void initData() {
        String appKey = APPKEY
        Map<String, Object> config = new HashMap<>()
        config.put("appSecret", APPSECRET)
        config.put("pushPackage", PACKAGENAME)
        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.XIAOMI.toString())
        xiaomiNoticeConfigurator.post(appKey, config, request)
    }

    @After
    void destroyData() {
        String appKey = APPKEY
        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.XIAOMI.toString())
        xiaomiNoticeConfigurator.delete(appKey, request)
    }

    @Test
    void testSendMessage() {
        def notice = new MockPushNotice()
        notice.setTargetTo(PUSHTOKEN)
        notice.setAppKey(APPKEY)
        notice.setTitle("test")
        notice.setContent("test")
        xiaomiNoticeSender.beforeSend(notice)
        xiaomiNoticeSender.send(notice)
        xiaomiNoticeSender.afterSend(notice)
        BusinessNoticeResult businessNoticeResult = xiaomiNoticeSender.getStatus(notice)
        if (NoticeStatus.FAIL == businessNoticeResult.getNoticeStatus()) {
            throw new ErrMsgException(businessNoticeResult.getMessage())
        }
        assert NoticeStatus.SUCCESS == businessNoticeResult.getNoticeStatus()
    }

    @Test
    void testSendLongTitleMessage() {
        def notice = new MockPushNotice()
        notice.setTargetTo(PUSHTOKEN)
        notice.setAppKey(APPKEY)
        def count = 100
        def a = ""
        count.times {
            a = a + "1"
        }
        notice.setTitle(a)
        notice.setContent("test")
        xiaomiNoticeSender.beforeSend(notice)
        xiaomiNoticeSender.send(notice)
        xiaomiNoticeSender.afterSend(notice)
        BusinessNoticeResult businessNoticeResult = xiaomiNoticeSender.getStatus(notice)
        if (NoticeStatus.FAIL == businessNoticeResult.getNoticeStatus()) {
            throw new ErrMsgException(businessNoticeResult.getMessage())
        }
        assert NoticeStatus.SUCCESS == businessNoticeResult.getNoticeStatus()
    }

    @Test
    void testPassThrough() {
        def notice = new MockPushNotice()
        notice.setAppKey(APPKEY)
        notice.setTitle("test")
        notice.setContent("test")
        notice.setTargetTo(PUSHTOKEN)
        Map<String, Object> customs = new HashMap()
        customs.put("_proper_badge", 5)
        customs.put("_proper_pushtype", "cmd")
        notice.setAllNoticeExtMsg(customs)
        xiaomiNoticeSender.send(notice)
    }

    @Test
    void invalidTargetTest() {
        def notice = new MockPushNotice()
        notice.setAppKey(APPKEY)
        notice.setTitle("test")
        notice.setContent("test")
        notice.setTargetTo("1231231")
        Map<String, Object> customs = new HashMap()
        customs.put("_proper_badge", 5)
        customs.put("_proper_pushtype", "cmd")
        notice.setAllNoticeExtMsg(customs)
        assert XiaomiErrCodeEnum.INVALID_TARGET.getNoticeCode() == xiaomiNoticeSender.send(notice).getCode()
    }

}
