package com.proper.enterprise.platform.pay.ali.service.impl

import com.proper.enterprise.platform.api.pay.enums.PayResType
import com.proper.enterprise.platform.api.pay.factory.PayFactory
import com.proper.enterprise.platform.api.pay.model.PrepayReq
import com.proper.enterprise.platform.api.pay.model.RefundReq
import com.proper.enterprise.platform.api.pay.service.PayService
import com.proper.enterprise.platform.pay.ali.entity.AliRefundEntity
import com.proper.enterprise.platform.pay.ali.model.AliPayResultRes
import com.proper.enterprise.platform.pay.ali.model.AliPayTradeQueryRes
import com.proper.enterprise.platform.pay.ali.model.AliRefundRes
import com.proper.enterprise.platform.pay.ali.model.AliRefundTradeQueryRes
import com.proper.enterprise.platform.pay.ali.repository.AliRefundRepository
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AliPayServiceImplTest extends AbstractJPATest {

    @Autowired
    PayFactory payFactory

    @Autowired
    AliRefundRepository aliRefundRepo

    private String payWay = "ali"

    @Autowired
    PayService payService

    @Test
    public void testPrepay() {
        PrepayReq prepayReq = new PrepayReq()
        prepayReq.setOutTradeNo("12345678901234567890")
        prepayReq.setTotalFee("123")
        prepayReq.setPayWay(payWay)
        prepayReq.setPayIntent("预约挂号")
        prepayReq.setOverMinuteTime("31")
        AliPayResultRes res = (AliPayResultRes)payService.savePrepay(prepayReq)

        assert res.getResultCode().equals(PayResType.SUCCESS)
        assert res.getPayInfo().contains("total_fee=\"1.23\"")
        assert res.getPayInfo().contains("body=\"预约挂号\"")
        assert res.getPayInfo().contains("out_trade_no=\"12345678901234567890\"")
    }

    @Test
    public void testQueryPay() {
        String outTradeNo = "201512191483583349219"
        AliPayTradeQueryRes queryRes = (AliPayTradeQueryRes)payService.queryPay(outTradeNo)

        assert queryRes.getOutTradeNo().equals(outTradeNo)
        assert queryRes.getCode().equals("10000")
        assert queryRes.getMsg().equals("Success")
        assert queryRes.getTotalAmount().equals("0.05")
    }

    @Test
    public void testRefundPay() {
        RefundReq refundReq = new RefundReq()
        refundReq.setOutTradeNo("201512191483583349219")
        refundReq.setOutRequestNo("20151219148358334921901")
        refundReq.setTotalFee("5")
        refundReq.setRefundAmount("1")
        AliRefundRes refundRes = (AliRefundRes)payService.refundPay(refundReq)
        AliRefundEntity refundInfo = aliRefundRepo.findByRefundNo("20151219148358334921901")

        assert refundRes.getCode().equals("10000")
        assert refundRes.getMsg().equals("Success")
        assert refundRes.getRefundFee().equals("0.01")
        assert refundRes.getOutTradeNo().equals("201512191483583349219")

        assert refundInfo.getCode().equals("10000")
        assert refundInfo.getMsg().equals("Success")
        assert refundInfo.getRefundFee().equals("0.01")
        assert refundInfo.getOutTradeNo().equals("201512191483583349219")
    }

    @Test
    public void testQueryRefund() {
        String orderNo = "201512191483583349219"
        String refundNo = "20151219148358334921901"
        AliRefundTradeQueryRes queryRefundRes = (AliRefundTradeQueryRes)payService.queryRefund(orderNo, refundNo)

        assert queryRefundRes.getCode().equals("10000")
        assert queryRefundRes.getMsg().equals("Success")
        assert queryRefundRes.getRefundAmount().equals("0.01")
    }
}
