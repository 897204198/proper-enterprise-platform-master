package com.proper.enterprise.platform.notice.server.push.factory

import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class PushSenderFactoryTest extends AbstractTest {

    @Test
    public void test() {
        assert null != PushSenderFactory.product(PushChannelEnum.IOS)
        assert null != PushSenderFactory.product(PushChannelEnum.HUAWEI)
        assert null != PushConfiguratorFactory.product(PushChannelEnum.XIAOMI)
    }
}
