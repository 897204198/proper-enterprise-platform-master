package com.proper.enterprise.platform.notice.server.push.factory

import PushChannelEnum
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class PushSenderFactoryTest extends AbstractTest {

    @Test
    public void test() {
        assert null != PushSenderFactory.product(PushChannelEnum.IOS)
    }
}
