package com.proper.enterprise.platform.common.pay.utils

import com.proper.enterprise.platform.api.pay.model.PrepayReq
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test

class PayUtilsTest extends AbstractJPATest {

    @Test
    public void testConvertMoneyFen2Yuan() {
        String yuanValue = PayUtils.convertMoneyFen2Yuan("101");
        assert "1.01".equals(yuanValue)
    }

    @Test
    public void testConvertMoneyYuan2Fen() {
        String yuanValue = PayUtils.convertMoneyYuan2Fen("1.23");
        assert "123".equals(yuanValue)
    }

    @Test
    public void testLogEntity() {
        PrepayReq prepayReq = new PrepayReq();
        prepayReq.setTotalFee("100")
        prepayReq.setOutTradeNo("123456")
        prepayReq.setPayIntent("预约挂号")
        prepayReq.setPayWay("wechat")
        prepayReq.setUserId("aaa")
        PayUtils.logEntity(prepayReq)
        assert "100".equals(prepayReq.getTotalFee())
        assert "123456".equals(prepayReq.getOutTradeNo())
        assert "预约挂号".equals(prepayReq.getPayIntent())
        assert "wechat".equals(prepayReq.getPayWay())
        assert "aaa".equals(prepayReq.getUserId())
    }
}
