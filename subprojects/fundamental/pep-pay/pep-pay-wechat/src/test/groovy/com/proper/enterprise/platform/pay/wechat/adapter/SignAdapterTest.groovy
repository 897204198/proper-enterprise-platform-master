package com.proper.enterprise.platform.pay.wechat.adapter

import com.proper.enterprise.platform.pay.wechat.model.WechatOrderReq
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test

class SignAdapterTest extends AbstractJPATest {

    @Test
    void testUnmarshal() {
        SignAdapter signAdapter = new SignAdapter();
        WechatOrderReq res = signAdapter.unmarshal("")
        assert res == null
    }
}
