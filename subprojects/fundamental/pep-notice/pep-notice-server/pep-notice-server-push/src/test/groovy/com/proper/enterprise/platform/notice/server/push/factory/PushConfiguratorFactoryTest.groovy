package com.proper.enterprise.platform.notice.server.push.factory

import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class PushConfiguratorFactoryTest extends AbstractTest{

    @Test
    public void test() {
        assert null != PushConfiguratorFactory.product(PushChannelEnum.APNS)
        assert null != PushConfiguratorFactory.product(PushChannelEnum.HUAWEI)
        assert null != PushConfiguratorFactory.product(PushChannelEnum.XIAOMI)
    }
}
