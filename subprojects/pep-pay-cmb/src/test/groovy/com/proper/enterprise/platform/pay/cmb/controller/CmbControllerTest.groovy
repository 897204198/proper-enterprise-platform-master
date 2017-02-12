package com.proper.enterprise.platform.pay.cmb.controller

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.pay.cmb.constants.CmbConstants
import com.proper.enterprise.platform.pay.cmb.document.CmbProtocolDocument
import com.proper.enterprise.platform.pay.cmb.entity.CmbPayEntity
import com.proper.enterprise.platform.pay.cmb.model.CmbRefundNoDupBodyReq
import com.proper.enterprise.platform.pay.cmb.model.CmbRefundNoDupRes
import com.proper.enterprise.platform.pay.cmb.repository.CmbProtocolRepository
import com.proper.enterprise.platform.pay.cmb.service.CmbPayService
import com.proper.enterprise.platform.pay.cmb.service.impl.CmbPayServiceImpl
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.oxm.Unmarshaller
import org.springframework.test.web.servlet.MvcResult

import javax.xml.transform.stream.StreamSource

class CmbControllerTest extends AbstractTest {

    @Autowired
    CmbPayService cmbPayService

    @Autowired
    CmbProtocolRepository cmbRepo

    @Autowired
    Map<String, Unmarshaller> unmarshallerMap;

    @Test
    public void noticeProtocolTest() {
        cmbRepo.deleteAll()
        CmbProtocolDocument userProtocolInfo = new CmbProtocolDocument()
        userProtocolInfo.setUserId("57ee2736ae65e2531aad70fa")
        userProtocolInfo.setSign(CmbConstants.CMB_PAY_PROTOCOL_UNSIGNED)
        cmbPayService.saveUserProtocolInfo(userProtocolInfo)
        post('/pay/cmb/noticeCmbProtocolInfo?RequestData={"NTBNBR":"P0015244","TRSCOD":"BKQY","DATLEN":"686","COMMID":"121Y612010001068xRGGZNFLKSMWXQIUNZMOCHB","BUSDAT":"PHhtbD48Y3VzdF9hcmdubz4yMDE2MTIwMTE2MTQyMzQ5MTAyMjwvY3VzdF9hcmdubz48cmVzcGNvZD5DTUJNQjk5PC9yZXNwY29kPjxyZXNwbXNnPuetvue9suWNj+iuruaIkOWKnzwvcmVzcG1zZz48bm90aWNlcGFyYT5wbm89MjAxNjEyMDExNjE0MjM0OTEwMjJ8dXNlcmlkPTU3ZWUyNzM2YWU2NWUyNTMxYWFkNzBmYTwvbm90aWNlcGFyYT48Y3VzdF9uby8+PGN1c3RfcGlkdHk+MTwvY3VzdF9waWR0eT48Y3VzdF9vcGVuX2RfcGF5Pk48L2N1c3Rfb3Blbl9kX3BheT48Y3VzdF9waWRfdj4xNzc0Njg1MjU0MzAyNTQ1NjcwMzA3NzUxNzgwODU8L2N1c3RfcGlkX3Y+PC94bWw+","SIGTIM":"201612011614550360","SIGDAT":"JoAABjRU+yMRHZ7vUZgWwHuykp6jyU3z1rHqg6JFeKKawY2+BAP/mMZ8dETZNly6GoyYuiDXG+fSt/oiuBnqBJIAaWKovg8lebe4qOUR11nElXbC3ZHml0fZetsM3RfTqwTbXKXIpfh1d8g0AZCXdoGGpBmRYF4mrLfVUR1du1Q="}', '', HttpStatus.OK)

        CmbProtocolDocument cmbUserInfo = cmbPayService.getUserProtocolInfo("57ee2736ae65e2531aad70fa")

        assert cmbUserInfo.getSign().equals("1")

        post('/pay/cmb/noticeCmbProtocolInfo?RequestData={"NTBNBR":"P0015244","TRSCOD":"BKQY","DATLEN":"686","COMMID":"121Y612010001068xRGGZNFLKSMWXQIUNZMOCHB","BUSDAT":"PHhtbD48Y3VzdF9hcmdubz4yMDE2MTIwMTE2MTQyMzQ5MTAyMjwvY3VzdF9hcmdubz48cmVzcGNvZD5DTUJNQjk5PC9yZXNwY29kPjxyZXNwbXNnPuetvue9suWNj+iuruaIkOWKnzwvcmVzcG1zZz48bm90aWNlcGFyYT5wbm89MjAxNjEyMDExNjE0MjM0OTEwMjJ8dXNlcmlkPTU3ZWUyNzM2YWU2NWUyNTMxYWFkNzBmYTwvbm90aWNlcGFyYT48Y3VzdF9uby8+PGN1c3RfcGlkdHk+MTwvY3VzdF9waWR0eT48Y3VzdF9vcGVuX2RfcGF5Pk48L2N1c3Rfb3Blbl9kX3BheT48Y3VzdF9waWRfdj4xNzc0Njg1MjU0MzAyNTQ1NjcwMzA3NzUxNzgwODU8L2N1c3RfcGlkX3Y+PC94bWw+","SIGTIM":"201612011614550360","SIGDAT":"JoAABjRU+yMRHZ7vUZgWwHuykp6jyU3z1rHqg6JFeKKawY2+BAP/mMZ8dETZNly6GoyYuiDXG+fSt/oiuBnqBJIAaWKovg8lebe4qOUR11nElXbC3ZHml0fZetsM3RfTqwTbXKXIpfh1d8g0AZCXdoGGpBmRYF4mrLfVUR1du1Q="}', '', HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    public void noticePayTest() {
        get("/pay/cmb/noticeCmbPayInfo?Succeed=Y&CoNo=000062&BillNo=7253638968&Amount=0.01&Date=20161201&MerchantPara=pno=20161201161423491022|userid=57ee2736ae65e2531aad70fa|intent=yuyueguahaojiaofei&Msg=00240000622016120116320165000000000020&Signature=85|43|105|137|32|31|227|23|199|171|181|183|148|207|191|96|207|12|22|128|229|187|215|238|158|145|70|128|27|64|53|5|180|174|217|159|84|159|103|251|223|64|47|197|103|109|199|71|182|41|67|250|141|170|90|66|113|83|156|168|239|225|186|232|", HttpStatus.OK);

        waitExecutorDone()
        CmbPayEntity cmbPayInfo = cmbPayService.getPayNoticeInfoByMsg("00240000622016120116320165000000000020")
        CmbPayEntity businessInfo = cmbPayService.findByOutTradeNo("201701011234567890")

        assert cmbPayInfo.getUserId().equals("57ee2736ae65e2531aad70fa")
        assert cmbPayInfo.getMerchantPara().contains("intent=yuyueguahaojiaofei")
        assert cmbPayInfo.getIntent().equals("yuyueguahaojiaofei")
        assert cmbPayInfo.getAmount().equals("0.01")

        assert businessInfo.getUserId().equals("testUserId")
        assert businessInfo.getIntent().equals("yuyueguahaojiaofei")
        assert businessInfo.getBillNo().equals("1234567890")
    }

    @Test
    public void queryPayTest() {
        CmbPayEntity payInfo = new CmbPayEntity()
        payInfo.setBillNo("1481260647")
        payInfo.setDate("20161209")
        MvcResult result = post('/pay/cmb/queryCmbPay', JSONUtil.toJSON(payInfo), HttpStatus.CREATED)
        String obj = result.getResponse().getContentAsString()
        Map<String, Object> doc = (Map<String, Object>)JSONUtil.parse(obj, Object.class)

        assert doc.get("resultCode") == "SUCCESS"

        payInfo.setBillNo("1481254422")
        payInfo.setDate("20161209")
        MvcResult resultErr = post('/pay/cmb/queryCmbPay', JSONUtil.toJSON(payInfo), HttpStatus.CREATED)
        String objErr = resultErr.getResponse().getContentAsString()
        Map<String, Object> docErr = (Map<String, Object>)JSONUtil.parse(objErr, Object.class)

        assert docErr.get("resultCode") == "SYSERROR"
    }

    @Test
    public void refundPayTest() {
        CmbRefundNoDupBodyReq refundInfo = new CmbRefundNoDupBodyReq()
        refundInfo.setAmount("1")
        refundInfo.setBillNo("2016120914812544221234")

        MvcResult result = post('/pay/cmb/refundCmbPay', JSONUtil.toJSON(refundInfo), HttpStatus.CREATED)
        String obj = result.getResponse().getContentAsString()
        CmbRefundNoDupRes res = (CmbRefundNoDupRes)unmarshallerMap.get("unmarshallCmbRefundNoDupRes").unmarshal(new StreamSource(new ByteArrayInputStream(obj.getBytes())));

        assert StringUtil.isNull(res.getHead().getCode())
        assert res.getBody().getAmount().equals("0.01")
    }

    @Test
    public void testSign() {
        CmbPayServiceImpl cmbPayServiceImpl = new CmbPayServiceImpl();
        assert !cmbPayServiceImpl.isValid("test")
    }
}
