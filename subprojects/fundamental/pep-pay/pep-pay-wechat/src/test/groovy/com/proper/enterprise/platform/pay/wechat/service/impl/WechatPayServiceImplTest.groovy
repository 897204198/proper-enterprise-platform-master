package com.proper.enterprise.platform.pay.wechat.service.impl

import com.proper.enterprise.platform.api.pay.enums.PayResType
import com.proper.enterprise.platform.api.pay.factory.PayFactory
import com.proper.enterprise.platform.api.pay.model.PrepayReq
import com.proper.enterprise.platform.api.pay.model.RefundReq
import com.proper.enterprise.platform.api.pay.service.PayService
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.pay.wechat.entity.WechatRefundEntity
import com.proper.enterprise.platform.pay.wechat.model.WechatPayQueryRes
import com.proper.enterprise.platform.pay.wechat.model.WechatPayResultRes
import com.proper.enterprise.platform.pay.wechat.model.WechatRefundQueryRes
import com.proper.enterprise.platform.pay.wechat.model.WechatRefundRes
import com.proper.enterprise.platform.pay.wechat.service.WechatPayService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class WechatPayServiceImplTest extends AbstractJPATest {

    @Autowired
    PayFactory payFactory

    @Autowired
    WechatPayService wechatPayService

    private String payWay = "wechat"

    @Test
    public void testPrepay() {
        PayService payService = payFactory.newPayService(payWay)
        PrepayReq prepayReq = new PrepayReq()
        prepayReq.setOutTradeNo("12345678901234567890")
        prepayReq.setTotalFee("123")
        prepayReq.setPayWay(payWay)
        prepayReq.setPayIntent("预约挂号")
        prepayReq.setPayTime(DateUtil.toString(new Date(), "yyyyMMddHHmmss"))
        prepayReq.setOverMinuteTime("30")

        WechatPayResultRes res = (WechatPayResultRes)payService.savePrepay(prepayReq)

        assert res.getResultCode().equals(PayResType.SUCCESS)
        assert StringUtil.isNotNull(res.getPrepayid())

        prepayReq.setOutTradeNo("01234567890123456789")
        WechatPayResultRes resErr = (WechatPayResultRes)payService.savePrepay(prepayReq)

        assert resErr.getResultCode().equals(PayResType.SYSERROR)
        assert StringUtil.isNull(resErr.getPrepayid())
    }

    @Test
    public void testQueryPay() {
        PayService payService = payFactory.newPayService(payWay)
        String outTradeNo = "201512191484099315532"
        WechatPayQueryRes res = payService.queryPay(outTradeNo)

        assert res.getReturnCode().equals("SUCCESS")
        assert res.getTradeState().equals("SUCCESS")
        assert res.getOutTradeNo().equals("201512191484099315532")
        assert res.getTotalFee().equals("5")
    }

    @Test
    public void testRefundPay() {
        PayService payService = payFactory.newPayService(payWay)
        RefundReq refundReq = new RefundReq()
        refundReq.setOutTradeNo("201512191484099315532")
        refundReq.setOutRequestNo("20151219148409931553201")
        refundReq.setTotalFee("5")
        refundReq.setRefundAmount("1")

        WechatRefundRes refundRes = (WechatRefundRes)payService.refundPay(refundReq)
        WechatRefundEntity refundInfo = wechatPayService.findByRefundNo("20151219148409931553201")

        assert refundRes.getReturnCode().equals("SUCCESS")
        assert refundRes.getOutTradeNo().equals("201512191484099315532")
        assert refundRes.getOutRefundNo().equals("20151219148409931553201")
        assert refundRes.getTotalFee().equals("5")
        assert refundRes.getRefundFee().equals("1")

        assert refundInfo.getReturnCode().equals("SUCCESS")
        assert refundInfo.getOutTradeNo().equals("201512191484099315532")
        assert refundInfo.getOutRefundNo().equals("20151219148409931553201")
        assert refundInfo.getTotalFee().equals("5")
        assert refundInfo.getRefundFee().equals("1")
    }

    @Test
    public void testRefundQuery() {
        PayService payService = payFactory.newPayService(payWay)
        String orderNo = "201512191484099315532"
        String refundNo = "20151219148409931553201"
        WechatRefundQueryRes refundQueryRes = (WechatRefundQueryRes)payService.queryRefund(orderNo, refundNo)

        assert refundQueryRes.getReturnCode().equals("SUCCESS")
        assert refundQueryRes.getRefundStatus().equals("SUCCESS")
        assert refundQueryRes.getRefundFee().equals("1")
    }

}
