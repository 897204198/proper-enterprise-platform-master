package com.proper.enterprise.platform.notice.sms.configurator

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class SMSNoticeConfiguratorTest extends AbstractTest {

    @Autowired
    private NoticeConfigurator smsNoticeConfigurator

    @Test
    void testAll() {
        addData()
        updateData()
        assert false == smsNoticeConfigurator.get('appkey').isEmpty()
        assert false == smsNoticeConfigurator.get('icmp').isEmpty()
        deleteData('icmp')
        assert null == smsNoticeConfigurator.get('icmp')
    }

    void addData() {
        def config = [:]
        config.put('smsUrl', 'test')
        config.put('smsSend', 'test')
        config.put('smsCharset', 'UTF-8')

        Map res = smsNoticeConfigurator.post('appkey', config)
        assert res.get('appKey') == 'appkey'
        assert res.get('smsUrl') == 'test'
        assert res.get('smsSend') == 'test'
        assert res.get('smsCharset') == 'UTF-8'

        config.put('smsUrl', 'test')
        config.put('smsSend', 'test')
        config.put('smsCharset', 'UTF-8')

        smsNoticeConfigurator.post('icmp', config)
    }

    void deleteData(String appKey) {
        smsNoticeConfigurator.delete(appKey)
    }

    void updateData() {
        def config = [:]
        config.put('smsUrl', 'test22')
        config.put('smsSend', 'test')
        config.put('smsCharset', 'UTF-8')

        Map res = smsNoticeConfigurator.put('appkey', config)
        assert res.get('smsUrl') == 'test22'
    }
}
