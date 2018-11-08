package com.proper.enterprise.platform.pay.ali.controller

import com.proper.enterprise.platform.api.pay.service.NoticeService
import com.proper.enterprise.platform.core.PEPApplicationContext
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.pay.ali.entity.AliEntity
import com.proper.enterprise.platform.pay.ali.model.AliOrderReq
import com.proper.enterprise.platform.pay.ali.repository.AliRepository
import com.proper.enterprise.platform.pay.ali.service.AliPayService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MvcResult

class AliControllerTest extends AbstractJPATest {

    @Autowired
    AliRepository aliRepository

    @Autowired
    AliPayService aliPayService

    @Test
    void noticeInfo() {

        post("/pay/ali/noticeAliPayInfo?sign_type=RSA&body=test&buyer_email=dreamer2618@gmail.com&buyer_id=2088902928162096&discount=0.00&gmt_create=2017-01-05 10:29:42&gmt_payment=2017-01-05 10:29:43&is_total_fee_adjust=N&notify_id=da1c584202b9be0b95ac4c957fb4905gp2&notify_time=2017-01-05 10:29:43&notify_type=trade_status_sync&out_trade_no=201512191483583349219&payment_type=1&price=0.05&quantity=1&seller_email=zfb_sj@sj-hospital.org&seller_id=2088021705112890&subject=test&total_fee=0.05&trade_no=2017010521001004090235281478&trade_status=TRADE_SUCCESS&use_coupon=N&sign=mqx7+KWUOqXeH0QqDHjQfyig4yVEqw704h7UpYHDIhP40Cr7ODRNSGnYja7RbVj6nowwH1LPxO3tI2x7+sSdOP9mBQT02A/rtxB83tsL3SHRj26yjKEEowKSbp1hiMTyfcI+1jG6ZbqWTSdMb7UXmwP47Rw888NkvKNrG5GE5cs=", '', HttpStatus.CREATED)

        waitExecutorDone()
        AliEntity noticeInfo = aliPayService.findByOutTradeNo("201512191483583349219")
        AliEntity businessInfo = aliPayService.findByOutTradeNo("001")
        AliEntity info = aliPayService.getByTradeNo("2017010521001004090235281478")

        assert noticeInfo.getBody().equals("test")
        assert noticeInfo.getTotalFee().equals("0.05")
        assert noticeInfo.getBuyerId().equals("2088902928162096")

        assert info.getBody() == "test"
        assert info.getTotalFee() == "0.05"
        assert info.getBuyerId() == "2088902928162096"

        assert businessInfo.getBody().equals("Async notice business")
        assert businessInfo.getBuyerId().equals("testNoticeBuyerId")
    }

    @Test
    void prepay() {
        String outTradeNo = DateUtil.toString(new Date(), "yyyyMMddHHmmssSSS").concat(RandomStringUtils.randomNumeric(15))
        AliOrderReq aliReq = new AliOrderReq()

        aliReq.setOutTradeNo(outTradeNo)
        aliReq.setTotalFee("1")
        aliReq.setBody("测试预支付")
        MvcResult result = post('/pay/ali/prepay', JSONUtil.toJSON(aliReq), HttpStatus.CREATED)

        String obj = result.getResponse().getContentAsString()
        Map<String, Object> doc = (Map<String, Object>)JSONUtil.parse(obj, Object.class)

        assert doc.get("resultCode") == "SUCCESS"

        aliReq.setTotalFee("-0.01")
        MvcResult resultErr = post('/pay/ali/prepay', JSONUtil.toJSON(aliReq), HttpStatus.CREATED)

        String objErr = resultErr.getResponse().getContentAsString()
        Map<String, Object> docErr = (Map<String, Object>)JSONUtil.parse(objErr, Object.class)

        assert docErr.get("resultCode") == "MONEYERROR"
    }

    @Test
    void checkDuplicate() {
        assert PEPApplicationContext.getBean(NoticeService.class).isDuplicate('test_order_no')
    }

}
