package com.proper.enterprise.platform.pay.wechat.adapter

import com.proper.enterprise.platform.pay.wechat.model.WechatOrderReq
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class SignAdapterTest extends AbstractTest {


    @Test
    public void testUnmarshal() {
        SignAdapter signAdapter = new SignAdapter();
        WechatOrderReq res = signAdapter.unmarshal("")
        assert res == null
    }
}
