package com.proper.enterprise.platform.common.refund.service.impl

import com.proper.enterprise.platform.api.refund.factory.RefundFactory
import com.proper.enterprise.platform.api.refund.service.RefundService
import com.proper.enterprise.platform.common.refund.MockRefundInfoReq
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AbstractRefundImplTest extends AbstractTest {

    @Autowired
    private RefundFactory refundFactory

    @Test
    void testRefund() {
        RefundService refundService = refundFactory.newRefundService("test")

        MockRefundInfoReq req = new MockRefundInfoReq()
        req.setStartDate("2016-01-01 00:00:00.000")
        req.setEndDate("2016-12-31 23:59:59.000")
        refundService.saveRefundInfoProcess(req)
    }

}
