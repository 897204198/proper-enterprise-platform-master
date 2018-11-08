package com.proper.enterprise.platform.pay.wechat.model

import com.proper.enterprise.platform.pay.wechat.entity.WechatEntity
import com.proper.enterprise.platform.pay.wechat.entity.WechatRefundEntity
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test

class BeanTest extends AbstractJPATest {

    @Test
    public void testBean() {
        coverBean(new WechatNoticeRes())
        coverBean(new WechatOrderReq())
        coverBean(new WechatOrderRes())
        coverBean(new WechatPayQueryReq())
        coverBean(new WechatPayQueryRes())
        coverBean(new WechatPayRes())
        coverBean(new WechatPayResultRes())
        coverBean(new WechatRefundQueryRes())
        coverBean(new WechatRefundReq())
        coverBean(new WechatRefundRes())
        coverBean(new WechatEntity())
        coverBean(new WechatRefundEntity())
    }

}
