package com.proper.enterprise.platform.common.pay.service.impl

import com.proper.enterprise.platform.api.pay.PayApiErrorProperties
import com.proper.enterprise.platform.api.pay.enums.PayResType
import com.proper.enterprise.platform.api.pay.factory.PayFactory
import com.proper.enterprise.platform.api.pay.model.PayResultRes
import com.proper.enterprise.platform.api.pay.model.PrepayReq
import com.proper.enterprise.platform.api.pay.model.RefundReq
import com.proper.enterprise.platform.api.pay.service.NoticeService
import com.proper.enterprise.platform.api.pay.service.PayService
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AbstractPayImplTest extends AbstractJPATest {

    @Autowired
    PayFactory payFactory

    @Autowired
    PayApiErrorProperties payApiErrorProperties

    private String payWay = "ali"

    @Test
    public void testPrepay() {
        PayService payService = payFactory.newPayService(payWay)

        // 请求对象为空
        PayResultRes res = payService.savePrepay(null)
        assert res.getResultCode() == PayResType.REQERROR
        assert res.getResultMsg().equals(payApiErrorProperties.getPayReq())

        // 请求对象订单号为空
        PrepayReq prepayReq = getReq()
        prepayReq.setOutTradeNo("")
        res = payService.savePrepay(prepayReq)
        assert res.getResultCode() == PayResType.ORDERNUMERROR
        assert res.getResultMsg().equals(payApiErrorProperties.getOrder())

        // 支付方式为空
        prepayReq = getReq()
        prepayReq.setPayWay("")
        res = payService.savePrepay(prepayReq)
        assert res.getResultCode() == PayResType.PAYWAYERROR
        assert res.getResultMsg().equals(payApiErrorProperties.getPayWay())

        // 支付金额为空
        prepayReq = getReq()
        prepayReq.setTotalFee("")
        res = payService.savePrepay(prepayReq)
        assert res.getResultCode() == PayResType.MONEYERROR
        assert res.getResultMsg().equals(payApiErrorProperties.getMoney())

        // 支付用途为空
        prepayReq = getReq()
        prepayReq.setPayIntent("")
        res = payService.savePrepay(prepayReq)
        assert res.getResultCode() == PayResType.PAYINTENTERROR
        assert res.getResultMsg().equals(payApiErrorProperties.getPayIntent())

        // 支付金额不正确:0
        prepayReq = getReq()
        prepayReq.setTotalFee("0")
        res = payService.savePrepay(prepayReq)
        assert res.getResultCode() == PayResType.MONEYERROR
        assert res.getResultMsg().equals(payApiErrorProperties.getMoney())

        // 支付金额不正确:负数
        prepayReq = getReq()
        prepayReq.setTotalFee("-1")
        res = payService.savePrepay(prepayReq)
        assert res.getResultCode() == PayResType.MONEYERROR
        assert res.getResultMsg().equals(payApiErrorProperties.getMoney())

        // 支付金额不正确:小数
        prepayReq = getReq()
        prepayReq.setTotalFee("0.01")
        res = payService.savePrepay(prepayReq)
        assert res.getResultCode() == PayResType.MONEYERROR
        assert res.getResultMsg().equals(payApiErrorProperties.getMoney())

        // 正常支付方式
        prepayReq = getReq()
        res = payService.savePrepay(prepayReq)
        assert res.getResultCode() == PayResType.SUCCESS
        assert res.getResultMsg().equals("success")

        // 异常支付方式
        prepayReq = getReq()
        prepayReq.setOutTradeNo("exception")
        res = payService.savePrepay(prepayReq)
        assert res.getResultCode() == PayResType.SYSERROR
        assert res.getResultMsg().equals(payApiErrorProperties.getSystem())
    }

    @Test
    public void testQueryPay() {
        PayService payService = payFactory.newPayService(payWay)
        String res = payService.queryPay("123456")
        assert res.equals("queryPaySuccess")
    }

    @Test
    public void testRefundPay() {
        PayService payService = payFactory.newPayService(payWay)
        RefundReq refundReq = new RefundReq()
        refundReq.setOutRequestNo("123456")
        String res = payService.refundPay(refundReq)
        assert res.equals("refundPaySuccess")

        refundReq = new RefundReq();
        assert StringUtil.isNull(payService.refundPay(refundReq))
    }

    @Test
    public void testQueryRefund() {
        PayService payService = payFactory.newPayService(payWay)
        String outTradeNo = "123456"
        String refundNo = "654321"
        String res = payService.queryRefund(outTradeNo, refundNo)
        assert res.equals("queryRefundSuccess")
    }

    @Test
    public void testNotice() {
        NoticeService payService = payFactory.newNoticeService(payWay)
        PayResultRes res = new PayResultRes()
        res.setResultCode(PayResType.SUCCESS)
        payService.saveNoticeProcessAsync(res)
    }

    @Test
    public void testNoticeException() {
        NoticeService payService = payFactory.newNoticeService(payWay)
        PayResultRes res = new PayResultRes()
        payService.saveNoticeProcessAsync(res)
    }

    private PrepayReq getReq() {
        PrepayReq prepayReq = new PrepayReq()
        prepayReq.setOutTradeNo("12345678901234567890")
        prepayReq.setTotalFee("100")
        prepayReq.setPayWay(payWay)
        prepayReq.setPayIntent("registration")
        return prepayReq
    }
}
