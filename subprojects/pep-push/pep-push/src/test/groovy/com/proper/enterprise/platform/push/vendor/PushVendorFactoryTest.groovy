package com.proper.enterprise.platform.push.vendor

import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType
import com.proper.enterprise.platform.push.common.model.enums.PushMode
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class PushVendorFactoryTest extends AbstractTest {

    @Autowired
    private PushVendorFactory factory

    @Test
    void test() {
        def service1 = factory.getPushVendorService('test1', PushDeviceType.ios, PushMode.apns)
        def service2 = factory.getPushVendorService('test2', PushDeviceType.ios, PushMode.apns)
        factory.getPushVendorService('test1', PushDeviceType.ios, PushMode.apns)
        assert service1 != null
        assert service2 != null
    }

}
