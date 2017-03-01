package com.proper.enterprise.platform.pay.cmb.service.impl

import com.proper.enterprise.platform.api.pay.enums.PayResType
import com.proper.enterprise.platform.api.pay.factory.PayFactory
import com.proper.enterprise.platform.api.pay.model.PayResultRes
import com.proper.enterprise.platform.api.pay.model.PrepayReq
import com.proper.enterprise.platform.api.pay.model.RefundReq
import com.proper.enterprise.platform.api.pay.service.PayService
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.pay.cmb.constants.CmbConstants
import com.proper.enterprise.platform.pay.cmb.document.CmbProtocolDocument
import com.proper.enterprise.platform.pay.cmb.entity.CmbRefundEntity
import com.proper.enterprise.platform.pay.cmb.model.CmbPayResultRes
import com.proper.enterprise.platform.pay.cmb.model.CmbQueryRefundRes
import com.proper.enterprise.platform.pay.cmb.model.CmbQuerySingleOrderRes
import com.proper.enterprise.platform.pay.cmb.model.CmbRefundNoDupRes
import com.proper.enterprise.platform.pay.cmb.repository.CmbProtocolRepository
import com.proper.enterprise.platform.pay.cmb.service.CmbPayService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class CmbPayServiceImplTest extends AbstractTest {

    @Autowired
    PayFactory payFactory

    @Autowired
    CmbPayService cmbPayService

    @Autowired
    CmbProtocolRepository cmbRepo

    private String payWay = "cmb"

    @Test
    public void testPrepayNeedSign() {
        cmbRepo.deleteAll()
        String dateInfo = DateUtil.toString(new Date(), CmbConstants.CMB_PAY_DATE_FORMAT_YYYYMMDD)
        PayService payService = payFactory.newPayService(payWay)
        PrepayReq prepayReq = new PrepayReq()
        prepayReq.setOutTradeNo(dateInfo + "1234567890")
        prepayReq.setTotalFee("123")
        prepayReq.setPayWay(payWay)
        prepayReq.setPayIntent("预约挂号缴费")
        prepayReq.setUserId("testUserId")
        prepayReq.setOverMinuteTime("30")
        PayResultRes payRes = payService.savePrepay(prepayReq)

        if(payRes.getResultCode().equals(PayResType.SUCCESS)) {
            CmbPayResultRes res = (CmbPayResultRes)payRes

            assert res.getCmbBillNo().equals("1234567890")
            assert res.getCmbDate().equals(dateInfo)
            assert res.getPayInfo().contains("intent=yuyueguahaojiaofei")
        } else {
            assert 0 == 1
        }

        prepayReq.setUserId(null)
        PayResultRes payResErr1 = payService.savePrepay(prepayReq)
        assert payResErr1.getResultCode().equals(PayResType.SYSERROR)
        assert payResErr1.getResultMsg().equals(CmbConstants.CMB_PAY_ERROR_USERID)

        prepayReq.setUserId("testUserId")
        prepayReq.setOutTradeNo("123")
        PayResultRes payResErr2 = payService.savePrepay(prepayReq)
        assert payResErr2.getResultCode().equals(PayResType.SYSERROR)
        assert payResErr2.getResultMsg().equals(CmbConstants.CMB_PAY_ERROR_BILLNO_ERROR)

    }

    @Test
    public void testPrepayNeedNotSign() {
        cmbRepo.deleteAll()
        CmbProtocolDocument userProtocolInfo = new CmbProtocolDocument()
        userProtocolInfo.setUserId("57ee2736ae65e2531aad70fa")
        userProtocolInfo.setProtocolNo("20150618113259999758")
        userProtocolInfo.setSign(CmbConstants.CMB_PAY_PROTOCOL_SIGNED)
        cmbPayService.saveUserProtocolInfo(userProtocolInfo)

        String dateInfo = DateUtil.toString(new Date(), CmbConstants.CMB_PAY_DATE_FORMAT_YYYYMMDD)
        PayService payService = payFactory.newPayService(payWay)
        PrepayReq prepayReq = new PrepayReq()
        prepayReq.setOutTradeNo(dateInfo + "1122334455")
        prepayReq.setTotalFee("321")
        prepayReq.setPayWay(payWay)
        prepayReq.setPayIntent("诊间缴费")
        prepayReq.setUserId("57ee2736ae65e2531aad70fa")

        PayResultRes payRes = payService.savePrepay(prepayReq)

        if(payRes.getResultCode().equals(PayResType.SUCCESS)) {
            CmbPayResultRes res = (CmbPayResultRes)payRes

            assert res.getCmbBillNo().equals("1122334455")
            assert res.getCmbDate().equals(dateInfo)
            assert res.getPayInfo().contains("intent=zhenjianjiaofei")
        } else {
            assert 0 == 1
        }
    }

    @Test
    public void testQueryPay() {
        PayService payService = payFactory.newPayService(payWay)
        String outTradeNo = "2016120914812606471234"
        CmbQuerySingleOrderRes res = payService.queryPay(outTradeNo)

        assert StringUtil.isNull(res.getHead().getCode())
        assert res.getBody().getBillNo().equals("1481260647")
        assert res.getBody().getBillAmount().equals("0.02")

        String outTradeNoErr = "2016120914812544221234"
        CmbQuerySingleOrderRes resErr = payService.queryPay(outTradeNoErr)

        assert StringUtil.isNotNull(resErr.getHead().getCode())
    }

    @Test
    public void testRefundPay() {
        PayService payService = payFactory.newPayService(payWay)
        RefundReq refundReq = new RefundReq()
        refundReq.setOutTradeNo("2016120914812544221234")
        refundReq.setOutRequestNo("20161209148125442201")
        refundReq.setRefundAmount("1")

        CmbRefundNoDupRes refundRes = (CmbRefundNoDupRes)payService.refundPay(refundReq)
        CmbRefundEntity refundInfo = cmbPayService.findByRefundNo("20161209148125442201")

        assert StringUtil.isNull(refundRes.getHead().getCode())
        assert refundRes.getBody().getAmount().equals("0.01")

        assert refundInfo.getRefundNo().equals("20161209148125442201")
        assert refundInfo.getAmount().equals("0.01")
        assert refundInfo.getReqBillNo().equals("1481254422")
    }

    @Test
    public void testRefundQuery() {
        PayService payService = payFactory.newPayService(payWay)
        String orderNo = "2016120914812544221234"
        String refundNo = "20161209148125442201"
        CmbQueryRefundRes refundQueryRes = (CmbQueryRefundRes)payService.queryRefund(orderNo, refundNo)

        assert StringUtil.isNull(refundQueryRes.getHead().getCode())
        assert refundQueryRes.getBody().getBillRecord().get(0).getAmount().equals("0.01")
    }
}
