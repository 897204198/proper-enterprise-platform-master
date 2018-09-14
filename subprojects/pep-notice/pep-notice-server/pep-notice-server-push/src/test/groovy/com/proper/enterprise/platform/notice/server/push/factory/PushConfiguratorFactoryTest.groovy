package com.proper.enterprise.platform.notice.server.push.factory

import PushChannelEnum
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class PushConfiguratorFactoryTest extends AbstractTest{

    @Test
    public void test() {
        assert null != PushConfiguratorFactory.product(PushChannelEnum.IOS)
    }
}
