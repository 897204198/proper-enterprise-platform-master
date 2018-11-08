package com.proper.enterprise.platform.pay.ali.model

import com.proper.enterprise.platform.pay.ali.entity.AliEntity
import com.proper.enterprise.platform.pay.ali.entity.AliRefundEntity
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test

class BeanTest extends AbstractJPATest {

    @Test
    public void testBean() {
        coverBean(new AliEntity())
        coverBean(new AliRefundEntity())
        coverBean(new AliOrderReq())
        coverBean(new AliPayResultRes())
        coverBean(new AliPayTradeQueryRes())
        coverBean(new AliRefundReq())
        coverBean(new AliRefundRes())
        coverBean(new AliRefundTradeQueryRes())
        coverBean(new AliBillRes())
    }

}
