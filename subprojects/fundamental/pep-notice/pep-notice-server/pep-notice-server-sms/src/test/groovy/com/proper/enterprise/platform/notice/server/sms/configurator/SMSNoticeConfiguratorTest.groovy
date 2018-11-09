package com.proper.enterprise.platform.notice.server.sms.configurator

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
        deleteData('appKey')
        deleteData('icmp')
        def config = [:]
        config.put('smsUrl', 'test')
        config.put('userId', 'userId')
        config.put('password', 'password')
        config.put('smsTemplate', 'test')
        config.put('smsCharset', 'UTF-8')

        Map res = smsNoticeConfigurator.post('appkey', config, null)
        assert res.get('appKey') == 'appkey'
        assert res.get('smsUrl') == 'test'
        assert res.get('userId') == 'userId'
        assert res.get('password') == 'password'
        assert res.get('smsTemplate') == 'UserId=userId&Password=password&Mobiles={0}&Content={1}'
        assert res.get('smsCharset') == 'UTF-8'

        config.put('smsUrl', 'test')
        config.put('smsTemplate', 'test')
        config.put('smsCharset', 'UTF-8')

        smsNoticeConfigurator.post('icmp', config, null)
    }

    void deleteData(String appKey) {
        smsNoticeConfigurator.delete(appKey, null)
    }

    void updateData() {
        def config = [:]
        config.put('smsUrl', 'test22')
        config.put('userId', 'userId1')
        config.put('password', 'password1')
        config.put('smsCharset', 'UTF-8')

        Map res = smsNoticeConfigurator.put('appkey', config, null)
        assert res.get('smsUrl') == 'test22'
        assert res.get('userId') == 'userId1'
        assert res.get('password') == 'password1'
        assert res.get('smsTemplate') == 'UserId=userId1&Password=password1&Mobiles={0}&Content={1}'
    }
}
