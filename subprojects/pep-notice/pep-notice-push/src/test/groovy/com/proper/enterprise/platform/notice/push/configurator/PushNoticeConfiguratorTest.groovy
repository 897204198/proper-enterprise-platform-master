package com.proper.enterprise.platform.notice.push.configurator

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class PushNoticeConfiguratorTest extends AbstractTest {

    @Autowired
    private NoticeConfigurator pushNoticeConfigurator

    @Test
    void testAll() {
        addData()
        updateData()
        assert false == pushNoticeConfigurator.get('appkey').isEmpty()
        assert false == pushNoticeConfigurator.get('icmp').isEmpty()
        deleteData('icmp')
        assert null == pushNoticeConfigurator.get('icmp')
    }

    void addData() {
        def config = [:]
        config.put('pushSecret', 'pep')
        config.put('pushPackageName', 'com.proper.pep.app')

        Map res = pushNoticeConfigurator.post('appkey', config)
        assert res.get('appKey') == 'appkey'
        assert res.get('pushSecret') == 'pep'
        assert res.get('pushPackageName') == 'com.proper.pep.app'

        config.put('pushSecret', 'pep')
        config.put('pushPackageName', 'com.proper.pep.app')

        pushNoticeConfigurator.post('icmp', config)
    }

    void deleteData(String appKey) {
        pushNoticeConfigurator.delete(appKey)
    }

    void updateData() {
        def config = [:]
        config.put('pushSecret', 'pep22')
        config.put('pushPackageName', 'com.proper.pep.app')

        Map res = pushNoticeConfigurator.put('appkey', config)
        assert res.get('pushSecret') == 'pep22'
    }
}
