package com.proper.enterprise.platform.notice.server.email.configurator

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class EmailNoticeConfiguratorTest extends AbstractTest {

    @Autowired
    private NoticeConfigurator emailNoticeConfigurator

    @Test
    void testAll() {
        addData()
        updateData()
        assert false == emailNoticeConfigurator.get('appkey',null).isEmpty()
        assert false == emailNoticeConfigurator.get('icmp',null).isEmpty()
        deleteData('icmp')
        assert null == emailNoticeConfigurator.get('icmp',null)
    }

    void addData() {
        deleteData("appkey")
        deleteData("icmp")

        def config = [:]
        config.put('mailServerHost', 'localhost')
        config.put('mailServerPort', 25)
        config.put('mailServerUsername', 'username')
        config.put('mailServerPassword', 'password')
        config.put('mailServerUseSSL', true)
        config.put('mailServerDefaultFrom', 'localhost@localhost')

        Map res = emailNoticeConfigurator.post('appkey', config,null)
        assert res.get('appKey') == 'appkey'
        assert res.get('mailServerHost') == 'localhost'
        assert res.get('mailServerPort') == 25
        assert res.get('mailServerUsername') == 'username'
        assert res.get('mailServerPassword') == 'password'
        assert res.get('mailServerUseSSL') == true
        assert res.get('mailServerUseTLS') == null
        assert res.get('mailServerDefaultFrom') == 'localhost@localhost'

        config.put('mailServerHost', 'icmp')
        config.put('mailServerPort', 25)
        config.put('mailServerUsername', 'username')
        config.put('mailServerPassword', 'password')
        config.put('mailServerUseTLS', true)
        config.put('mailServerDefaultFrom', 'localhost@localhost')

        emailNoticeConfigurator.post('icmp', config,null)
    }

    void deleteData(String appKey) {
        emailNoticeConfigurator.delete(appKey,null)
    }

    void updateData() {
        def config = [:]
        config.put('mailServerHost', 'localhost2')
        config.put('mailServerPort', 35)
        config.put('mailServerUsername', 'username2')
        config.put('mailServerPassword', 'password2')
        config.put('mailServerUseSSL', true)
        config.put('mailServerDefaultFrom', 'localhost@localhost')

        Map res = emailNoticeConfigurator.put('appkey', config,null)
        assert res.get('mailServerHost') == 'localhost2'
    }

}
