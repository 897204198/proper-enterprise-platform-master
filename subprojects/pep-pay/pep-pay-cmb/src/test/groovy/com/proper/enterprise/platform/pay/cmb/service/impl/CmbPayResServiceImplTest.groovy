package com.proper.enterprise.platform.pay.cmb.service.impl

import com.proper.enterprise.platform.pay.cmb.service.CmbPayService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.oxm.Marshaller
import org.springframework.oxm.Unmarshaller

class CmbPayResServiceImplTest extends AbstractTest {

    @Autowired
    Map<String, Unmarshaller> unmarshallerMap

    @Autowired
    Marshaller marshaller

    @Autowired
    CmbPayService cmbPayService

    @Test
    public void testResApi() {
//        CmbPayResServiceImpl cmbPayResServiceimpl = new CmbPayResServiceImpl()
//        cmbPayResServiceimpl.unmarshallerMap = unmarshallerMap
//
//        CmbQuerySingleOrderReq queryReq = new CmbQuerySingleOrderReq()
//        CmbQuerySingleOrderHeadReq headReq = new CmbQuerySingleOrderHeadReq()
//        CmbQuerySingleOrderBodyReq bodyReq = new CmbQuerySingleOrderBodyReq()
//        CmbPayEntity payInfo = cmbPayService.getQueryInfo("2016120814812606471234")
//
//        // 请求时间,精确到毫秒
//        headReq.setTimeStamp(CmbUtils.getCmbReqTime());
//        // 订单号
//        bodyReq.setBillNo(payInfo.getBillNo());
//        // 交易日期
//        bodyReq.setDate(payInfo.getDate());
//        // head
//        queryReq.setHead(headReq);
//        // body
//        queryReq.setBody(bodyReq);
//        // hash
//        StringWriter writer = new StringWriter();
//        marshaller.marshal(queryReq, new StreamResult(writer));
//        String preXML = CmbUtils.getOriginSign(writer.toString());
//        queryReq.setHash(CmbUtils.encrypt(preXML, "SHA-1"));
//
//        StringWriter writer1 = new StringWriter();
//        marshaller.marshal(queryReq, new StreamResult(writer1));
//        String requestXML = writer1.toString();
//
//        CmbQuerySingleOrderRes res = (CmbQuerySingleOrderRes)cmbPayResServiceimpl.getCmbApiRes(CmbConstants.CMB_PAY_DIRECT_REQUEST_X, "unmarshallCmbQuerySingleOrderRes", requestXML)
//
//        assert StringUtil.isNotNull(res.getHead().getCode())
        println 1
    }

}
