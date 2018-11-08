package com.proper.enterprise.platform.pay.web.ali.model

import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebEntity
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebRefundEntity
import org.junit.Test

class BeanTest extends AbstractJPATest {

    @Test
    public void testBean() {
        coverBean(new AliwebEntity())
        coverBean(new AliwebRefundEntity())
        coverBean(new AliwebOrderReq())
        coverBean(new AliwebPayResultRes())
        coverBean(new AliwebRefundReq())
    }
}
