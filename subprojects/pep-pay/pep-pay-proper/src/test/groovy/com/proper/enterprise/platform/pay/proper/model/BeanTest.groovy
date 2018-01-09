package com.proper.enterprise.platform.pay.proper.model

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class BeanTest extends AbstractTest {

    @Test
    public void testBean() {
        coverBean(new ProperBillRes())
        coverBean(new ProperOrderReq())
        coverBean(new ProperPayResultRes())
        coverBean(new ProperQueryRes())
        coverBean(new ProperRefundReq())
        coverBean(new ProperRefundRes())
        coverBean(new ProperRefundTradeQueryRes())
    }

}
