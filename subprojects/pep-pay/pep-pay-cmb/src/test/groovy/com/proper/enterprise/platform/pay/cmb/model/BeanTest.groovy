package com.proper.enterprise.platform.pay.cmb.model

import com.proper.enterprise.platform.pay.cmb.document.CmbProtocolDocument
import com.proper.enterprise.platform.pay.cmb.entity.CmbProtocolEntity
import com.proper.enterprise.platform.pay.cmb.entity.CmbQueryRefundEntity
import com.proper.enterprise.platform.pay.cmb.entity.CmbRefundEntity
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class BeanTest extends AbstractTest {

    @Test
    public void testBean() {
        coverBean(new CmbProtocolDocument())
        coverBean(new CmbProtocolEntity())
        coverBean(new CmbQueryRefundEntity())
        coverBean(new CmbRefundEntity())
        coverBean(new CmbBillRecordRes())
        coverBean(new CmbBusinessNoProReq())
        coverBean(new CmbBusinessProReq())
        coverBean(new CmbBusinessRes())
        coverBean(new CmbCommonHeadRes())
        coverBean(new CmbOrderReq())
        coverBean(new CmbPayResultRes())
        coverBean(new CmbQueryRefundBodyReq())
        coverBean(new CmbQueryRefundBodyRes())
        coverBean(new CmbQueryRefundHeadReq())

        coverBean(new CmbQueryRefundReq())
        coverBean(new CmbQueryRefundRes())
        coverBean(new CmbQuerySingleOrderBodyReq())
        coverBean(new CmbQuerySingleOrderBodyRes())
        coverBean(new CmbQuerySingleOrderHeadReq())
        coverBean(new CmbQuerySingleOrderReq())
        coverBean(new CmbQuerySingleOrderRes())

        coverBean(new CmbRefundNoDupBodyReq())
        coverBean(new CmbRefundNoDupBodyRes())
        coverBean(new CmbRefundNoDupHeadReq())
        coverBean(new CmbRefundNoDupReq())
        coverBean(new CmbRefundNoDupRes())
    }

}
