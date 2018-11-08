package com.proper.enterprise.platform.pay.wechat.controller

import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.pay.wechat.entity.WechatEntity
import com.proper.enterprise.platform.pay.wechat.service.WechatPayResService
import com.proper.enterprise.platform.pay.wechat.service.WechatPayService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MvcResult

class WechatControllerTest extends AbstractJPATest{

    @Autowired
    WechatPayService wechatPayService;

    @Autowired
    WechatPayResService wechatPayResService;

    @Test
    void testNoticeInfo() {
        def xml = """
<xml><appid><![CDATA[wxc7825f86d99f3d90]]></appid>
<bank_type><![CDATA[CFT]]></bank_type>
<cash_fee><![CDATA[1]]></cash_fee>
<device_info><![CDATA[WEB]]></device_info>
<fee_type><![CDATA[CNY]]></fee_type>
<is_subscribe><![CDATA[N]]></is_subscribe>
<mch_id><![CDATA[1379027502]]></mch_id>
<nonce_str><![CDATA[XEvuHhlpbwqtfrsS]]></nonce_str>
<openid><![CDATA[otNUJwrZN986eIejGuBbk2Opn-NY]]></openid>
<out_trade_no><![CDATA[2016112214104819158269]]></out_trade_no>
<result_code><![CDATA[SUCCESS]]></result_code>
<return_code><![CDATA[SUCCESS]]></return_code>
<sign><![CDATA[90DFEC5996AC27C5F1D49B4976B6ABF7]]></sign>
<time_end><![CDATA[20160911234650]]></time_end>
<total_fee>1</total_fee>
<trade_type><![CDATA[APP]]></trade_type>
<transaction_id><![CDATA[4001922001201609113720112951]]></transaction_id>
</xml>
"""
        post('/pay/wechat/noticeWechatPayInfo',MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON_UTF8,xml,HttpStatus.OK)

        waitExecutorDone()
        WechatEntity wechatInfo = wechatPayService.findByOutTradeNo("2016112214104819158269")
        WechatEntity businessInfo = wechatPayService.findByOutTradeNo("001")
        WechatEntity info = wechatPayService.getByTradeNo("4001922001201609113720112951")

        assert wechatInfo.getResultCode().equals("SUCCESS")
        assert wechatInfo.getOutTradeNo().equals("2016112214104819158269")
        assert wechatInfo.getAppid().equals("wxc7825f86d99f3d90")

        assert info.getResultCode() == "SUCCESS"
        assert info.getOutTradeNo() == "2016112214104819158269"
        assert info.getAppid() == "wxc7825f86d99f3d90"

        assert businessInfo.getAttach().equals("异步通知相关业务处理")
        assert businessInfo.getDeviceInfo().equals("test")
        assert businessInfo.getTotalFee().equals("123")
    }

    @Test
    void testWechatPrepay() {
        String outTradeNo = DateUtil.toString(new Date(), "yyyyMMddHHmmssSSS").concat(RandomStringUtils.randomNumeric(15))
        MvcResult result = post('/pay/wechat/prepay', '{"outTradeNo": ' + outTradeNo + ', "body": "测试预支付",  "totalFee": 1}', HttpStatus.CREATED)
        String obj = result.getResponse().getContentAsString()
        Map<String, Object> doc = (Map<String, Object>)JSONUtil.parse(obj, Object.class)

        assert doc.get("resultCode") == "SUCCESS"
        assert StringUtil.isNotNull(String.valueOf(doc.get("prepayid")))

        MvcResult resultErr = post('/pay/wechat/prepay', '{"outTradeNo": ' + outTradeNo + ', "body": "测试预支付",  "totalFee": 0}', HttpStatus.CREATED)
        String objErr = resultErr.getResponse().getContentAsString()
        Map<String, Object> docErr = (Map<String, Object>)JSONUtil.parse(objErr, Object.class)

        assert docErr.get("resultCode") == "MONEYERROR"

    }
}
