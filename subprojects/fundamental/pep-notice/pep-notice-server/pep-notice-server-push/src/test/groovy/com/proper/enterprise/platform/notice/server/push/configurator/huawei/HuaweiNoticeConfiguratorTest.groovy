package com.proper.enterprise.platform.notice.server.push.configurator.huawei

import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class HuaweiNoticeConfiguratorTest extends AbstractJPATest {

    @Autowired
    private NoticeConfigurator pushNoticeConfigurator

    @Test
    void testAll() {
        addData()
        updateData()
        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.HUAWEI.toString())
        assert !pushNoticeConfigurator.get('appkey', request).isEmpty()
        assert !pushNoticeConfigurator.get('icmp', request).isEmpty()
        deleteData('icmp')
        assert null == pushNoticeConfigurator.get('icmp', request)
    }

    void addData() {
        def config = [:]
        config.put('appSecret', 'cb5b99c684477aaa3b6a28b2c7cbe7b2')
        config.put('pushPackage', 'com.proper.icmp.dev')
        config.put('appId', '100213965')

        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.HUAWEI.toString())
        Map res = pushNoticeConfigurator.post('appkey', config, request)
        assert res.get('appSecret') == 'cb5b99c684477aaa3b6a28b2c7cbe7b2'
        assert res.get('pushPackage') == 'com.proper.icmp.dev'
        assert res.get('appId') == '100213965'

        config.put('appSecret', 'a31f53301ed9f45e94530235dd933d25')
        config.put('appId', '100029163')
        config.put('pushPackage', 'com.proper.icmp')

        pushNoticeConfigurator.post('icmp', config, request)

        try {
            config.put('appId', '')
            pushNoticeConfigurator.post('icmp2', config, request)
        } catch (ErrMsgException e) {
            e.getMessage().contains("appId can't be null")
        }
    }

    void deleteData(String appKey) {
        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.HUAWEI.toString())
        pushNoticeConfigurator.delete(appKey, request)
    }

    void updateData() {
        def config = [:]
        config.put('appSecret', 'a31f53301ed9f45e94530235dd933d25')
        config.put('pushPackage', 'com.proper.icmp')
        config.put('appId', '100029163')

        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.HUAWEI.toString())
        Map res = pushNoticeConfigurator.put('appkey', config, request)
        assert res.get('appSecret') == 'a31f53301ed9f45e94530235dd933d25'

        try {
            config.put('appSecret', 'XX')
            config.put('pushPackage', 'XXX.XXX.XXX.XXX')
            config.put('appId', 'XX')

            request = new HashMap()
            request.put("pushChannel", PushChannelEnum.HUAWEI.toString())
            pushNoticeConfigurator.put('appkey', config, request)
        } catch (Exception e) {
            e.getMessage().contains("Please check Huawei push config")
        }
    }
}
