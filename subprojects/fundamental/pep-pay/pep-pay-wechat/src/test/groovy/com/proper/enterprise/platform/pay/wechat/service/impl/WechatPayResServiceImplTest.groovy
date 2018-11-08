package com.proper.enterprise.platform.pay.wechat.service.impl

import com.proper.enterprise.platform.pay.wechat.PayWechatProperties
import com.proper.enterprise.platform.pay.wechat.adapter.SignAdapter
import com.proper.enterprise.platform.pay.wechat.model.WechatOrderReq
import com.proper.enterprise.platform.pay.wechat.model.WechatOrderRes
import com.proper.enterprise.platform.pay.wechat.model.WechatRefundReq
import com.proper.enterprise.platform.pay.wechat.model.WechatRefundRes
import com.proper.enterprise.platform.test.AbstractJPATest
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.oxm.Marshaller
import org.springframework.oxm.Unmarshaller

import javax.xml.transform.stream.StreamResult

class WechatPayResServiceImplTest extends AbstractJPATest {

    @Autowired
    Map<String, Unmarshaller> unmarshallerMap;

    @Autowired
    Marshaller marshaller

    @Autowired
    PayWechatProperties payWechatProperties

    @Test
    public void testHttpRequest() {
        WechatPayResServiceImpl wechatPayResServiceImpl = new WechatPayResServiceImpl()
        wechatPayResServiceImpl.unmarshallerMap = unmarshallerMap

        WechatOrderReq uoReq = new WechatOrderReq()
        uoReq.setOutTradeNo("12345678901234567890")
        uoReq.setDetail("预约挂号")
        uoReq.setBody("预约挂号")
        uoReq.setAttach("预约挂号")
        uoReq.setTotalFee(Integer.parseInt("123"))
        uoReq.setNonceStr(RandomStringUtils.randomAlphabetic(payWechatProperties.getRandomLen()))
        uoReq.setSpbillCreateIp("127.0.0.1")

        StringWriter writer = new StringWriter()
        marshaller.marshal(uoReq, new StreamResult(writer))
        String requestXML = writer.toString()

        WechatOrderRes res = wechatPayResServiceImpl.getWechatApiRes(payWechatProperties.getUrlUnified(), "unmarshallWechatOrderRes", requestXML, false)

        assert res.getReturnCode().equals("FAIL")
    }

//    @Test
    public void testHttpsRequest() {
        WechatPayResServiceImpl wechatPayResServiceImpl = new WechatPayResServiceImpl()
        wechatPayResServiceImpl.unmarshallerMap = unmarshallerMap

        WechatRefundReq wechatRefundReq = new WechatRefundReq()
        wechatRefundReq.setOutRefundNo("20151219148409931553201")
        wechatRefundReq.setRefundFee(1)
        wechatRefundReq.setTotalFee(5)
        wechatRefundReq.setOutTradeNo("201112191484099315532")
        wechatRefundReq.setNonceStr(RandomStringUtils.randomAlphabetic(payWechatProperties.getRandomLen()))

        // 签名
        SignAdapter signAdapter = new SignAdapter()
        String sign = signAdapter.marshalObject(wechatRefundReq, WechatRefundReq.class)
        wechatRefundReq.setSign(sign)

        StringWriter writer = new StringWriter();
        marshaller.marshal(wechatRefundReq, new StreamResult(writer));
        String requestXML = writer.toString();

        WechatRefundRes res = wechatPayResServiceImpl.getWechatApiRes(payWechatProperties.getUrlRefund(), "unmarshallWechatRefundRes", requestXML, true)

        assert res.getReturnCode().equals("SUCCESS")
        assert res.getResultCode().equals("FAIL")

    }
}
