package com.proper.enterprise.platform.pay.web.ali.service.impl

import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse
import com.alipay.api.response.AlipayTradeQueryResponse
import com.alipay.api.response.AlipayTradeRefundResponse
import com.proper.enterprise.platform.api.pay.enums.PayResType
import com.proper.enterprise.platform.api.pay.factory.PayFactory
import com.proper.enterprise.platform.api.pay.model.PrepayReq
import com.proper.enterprise.platform.api.pay.model.RefundReq
import com.proper.enterprise.platform.api.pay.service.PayService
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebRefundEntity
import com.proper.enterprise.platform.pay.web.ali.model.AliwebPayResultRes
import com.proper.enterprise.platform.pay.web.ali.repository.AliwebRefundRepository
import com.proper.enterprise.platform.pay.web.ali.service.AliwebPayResService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AliwebPayServiceImplTest extends AbstractJPATest {

    @Autowired
    PayFactory payFactory

    @Autowired
    AliwebRefundRepository aliwebRefundRepo

    private String payWay = "aliweb"

    @Autowired
    AliwebPayResService aliwebPayResService

    @Test
    void testPrepay() {
        PrepayReq prepayReq = new PrepayReq()
        prepayReq.setOutTradeNo("12345678901234567890")
        prepayReq.setTotalFee("123")
        prepayReq.setPayWay(payWay)
        prepayReq.setPayIntent("预约挂号")
        prepayReq.setOverMinuteTime("31")

        PayService payService = payFactory.newPayService(payWay)
        AliwebPayResultRes res = (AliwebPayResultRes)payService.savePrepay(prepayReq)

        assert res.getResultCode().equals(PayResType.SUCCESS)
        assert res.getForm().contains("预约挂号")
        assert res.getForm().contains("12345678901234567890")
        assert res.getForm().contains("1.23")
    }

    @Test
    void testQueryPay() {
        String outTradeNo = "20170525211121474"
        PayService payService = payFactory.newPayService(payWay)
        AlipayTradeQueryResponse queryRes = (AlipayTradeQueryResponse)payService.queryPay(outTradeNo)

        assert queryRes.getOutTradeNo().equals(outTradeNo)
        assert queryRes.getCode().equals("10000")
        assert queryRes.getMsg().equals("Success")
        assert queryRes.getTotalAmount().equals("0.03")
    }

    @Test
    void testRefundPay() {
        RefundReq refundReq = new RefundReq()
        refundReq.setOutTradeNo("20170525211121474")
        refundReq.setOutRequestNo("2017052521112147401")
        refundReq.setTotalFee("3")
        refundReq.setRefundAmount("1")
        PayService payService = payFactory.newPayService(payWay)
        AlipayTradeRefundResponse refundRes = (AlipayTradeRefundResponse)payService.refundPay(refundReq)
        AliwebRefundEntity refundInfo = aliwebRefundRepo.findByRefundNo("2017052521112147401")

        assert refundRes.getCode().equals("10000")
        assert refundRes.getMsg().equals("Success")
        assert refundRes.getRefundFee().equals("0.01")
        assert refundRes.getOutTradeNo().equals("20170525211121474")

        assert refundInfo.getCode().equals("10000")
        assert refundInfo.getMsg().equals("Success")
        assert refundInfo.getRefundFee().equals("0.01")
        assert refundInfo.getOutTradeNo().equals("20170525211121474")
    }

    @Test
    void testQueryRefund() {
        String orderNo = "20170525211121474"
        String refundNo = "2017052521112147401"
        PayService payService = payFactory.newPayService(payWay)
        AlipayTradeFastpayRefundQueryResponse queryRefundRes = (AlipayTradeFastpayRefundQueryResponse)payService.queryRefund(orderNo, refundNo)

        assert queryRefundRes.getCode().equals("10000")
        assert queryRefundRes.getMsg().equals("Success")
        assert queryRefundRes.getRefundAmount().equals("0.01")
    }
}
