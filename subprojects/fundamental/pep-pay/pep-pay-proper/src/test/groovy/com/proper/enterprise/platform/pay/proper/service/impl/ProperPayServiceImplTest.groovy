package com.proper.enterprise.platform.pay.proper.service.impl

import com.proper.enterprise.platform.api.pay.enums.PayResType
import com.proper.enterprise.platform.api.pay.factory.PayFactory
import com.proper.enterprise.platform.api.pay.model.PrepayReq
import com.proper.enterprise.platform.api.pay.model.RefundReq
import com.proper.enterprise.platform.api.pay.service.PayService
import com.proper.enterprise.platform.pay.proper.entity.ProperEntity
import com.proper.enterprise.platform.pay.proper.entity.ProperRefundEntity
import com.proper.enterprise.platform.pay.proper.model.ProperPayResultRes
import com.proper.enterprise.platform.pay.proper.model.ProperQueryRes
import com.proper.enterprise.platform.pay.proper.model.ProperRefundRes
import com.proper.enterprise.platform.pay.proper.model.ProperRefundTradeQueryRes
import com.proper.enterprise.platform.pay.proper.repository.ProperRefundRepository
import com.proper.enterprise.platform.pay.proper.service.ProperPayService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ProperPayServiceImplTest extends AbstractJPATest {

    @Autowired
    PayFactory payFactory

    @Autowired
    ProperRefundRepository properRefundRepo

    private String payWay = "proper"

    @Autowired
    ProperPayService properPayService

    @Test
    public void testPrepay() {
        PrepayReq prepayReq = new PrepayReq()
        prepayReq.setOutTradeNo("12345678901234567890")
        prepayReq.setTotalFee("123")
        prepayReq.setPayWay(payWay)
        prepayReq.setPayIntent("预约挂号")
        prepayReq.setOverMinuteTime("31")
        PayService payService = payFactory.newPayService(payWay)
        ProperPayResultRes res = (ProperPayResultRes)payService.savePrepay(prepayReq)

        assert res.getResultCode().equals(PayResType.SUCCESS)
        assert res.getPayInfo().contains("\"totalFee\":\"1.23\"")
        assert res.getPayInfo().contains("\"outTradeNo\":\"12345678901234567890\"")
    }

    @Test
    public void testQueryPay() {
        String outTradeNo = "201512191483583349219"
        PayService payService = payFactory.newPayService(payWay)
        ProperQueryRes queryRes = (ProperQueryRes)payService.queryPay(outTradeNo)

        assert queryRes.getCode() == '10000'
    }

    @Test
    public void testRefundPay() {
        RefundReq refundReq = new RefundReq()
        refundReq.setOutTradeNo("201512191483583349219")
        refundReq.setOutRequestNo("20151219148358334921901")
        refundReq.setTotalFee("5")
        refundReq.setRefundAmount("1")
        PayService payService = payFactory.newPayService(payWay)
        ProperRefundRes refundRes = (ProperRefundRes)payService.refundPay(refundReq)
        ProperRefundEntity refundInfo = properRefundRepo.findByRefundNo("20151219148358334921901")

        assert refundRes.getCode().equals("10000")
        assert refundRes.getMsg().equals("SUCCESS")

        assert refundInfo.getCode().equals("10000")
        assert refundInfo.getMsg().equals("SUCCESS")
        assert refundInfo.getRefundFee().equals("0.01")
        assert refundInfo.getOutTradeNo().equals("201512191483583349219")
    }

    @Test
    public void testQueryRefund() {
        String orderNo = "201512191483583349219"
        String refundNo = "20151219148358334921901"
        PayService payService = payFactory.newPayService(payWay)
        ProperRefundTradeQueryRes queryRefundRes = (ProperRefundTradeQueryRes)payService.queryRefund(orderNo, refundNo)

        assert queryRefundRes.getCode().equals("10000")
        assert queryRefundRes.getMsg().equals("SUCCESS")
    }

    @Test
    void testProperServiceImplOfMethod(){
        ProperEntity properEntity = new ProperEntity()
        properEntity.setNotifyTime("time")
        properEntity.setOutTradeNo("number")
        properEntity.setTotalFee("fee")
        properEntity = properPayService.save(properEntity)
        ProperRefundEntity properRefundEntity = new ProperRefundEntity()
        properRefundEntity.setCode("code")
        properRefundEntity.setMsg("msg")
        properRefundEntity.setTradeNo("tradeNo")
        properRefundEntity.setOutTradeNo("outTradeNo")
        properRefundEntity.setRefundNo("refundNo")
        properRefundEntity.setRefundFee("refundFee")
        properRefundEntity = properPayService.save(properRefundEntity)
        properPayService.findByOutTradeNo(properEntity.getOutTradeNo())
        properPayService.getByTradeNo(properEntity.getOutTradeNo())
        properPayService.findByRefundNo(properRefundEntity.getRefundNo())
    }
}
