package com.proper.enterprise.platform.pay.wechat.service.impl

import com.proper.enterprise.platform.pay.wechat.PayWechatProperties
import com.proper.enterprise.platform.pay.wechat.model.WechatOrderReq
import com.proper.enterprise.platform.pay.wechat.model.WechatOrderRes
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
    void testHttpRequest() {
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

}
