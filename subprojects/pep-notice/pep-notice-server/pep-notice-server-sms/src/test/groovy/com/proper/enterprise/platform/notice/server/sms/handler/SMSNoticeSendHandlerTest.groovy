package com.proper.enterprise.platform.notice.server.sms.handler

import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.api.model.Notice
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.notice.server.sms.entity.MockNotice
import com.proper.enterprise.platform.sys.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class SMSNoticeSendHandlerTest extends AbstractTest {

    @Autowired
    private NoticeSendHandler smsNoticeSender

    @Autowired
    private NoticeConfigurator smsNoticeConfigurator

    @Test
    void testSendSMS() {
        try{
            def config = [:]
            config.put('smsUrl', 'http://localhost:8080/smsservice/SendSMS')
            config.put('smsSend', 'UserId=****&Password=****&Mobiles={0}&Content={1}')
            config.put('smsCharset', 'GBK')
            smsNoticeConfigurator.post('pep', config)
            Notice noticeOperation = new MockNotice()
            noticeOperation.setAppKey('pep')
            noticeOperation.setTargetTo('18502410459')
            noticeOperation.setContent("测试短信 - ${DateUtil.timestamp}")
            smsNoticeSender.beforeSend(noticeOperation)
            smsNoticeSender.send(noticeOperation)
            sleep(3000)
        } catch (Exception e) {
            assert e.getMessage().contains(I18NUtil.getMessage("pep.notice.sms.send.error"))
        }
    }

    @Test
    void testAfterSend() {
        Notice noticeOperation = new MockNotice()
        noticeOperation.setAppKey('pep')
        noticeOperation.setTargetTo('18502410459')
        noticeOperation.setContent("测试短信 - ${DateUtil.timestamp}")
        smsNoticeSender.afterSend(noticeOperation)
        assert NoticeStatus.SUCCESS == smsNoticeSender.getStatus(noticeOperation)
    }
}
