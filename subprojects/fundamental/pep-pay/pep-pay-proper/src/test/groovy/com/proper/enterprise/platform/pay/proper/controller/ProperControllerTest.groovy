package com.proper.enterprise.platform.pay.proper.controller

import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.pay.proper.entity.ProperEntity
import com.proper.enterprise.platform.pay.proper.model.ProperOrderReq
import com.proper.enterprise.platform.pay.proper.repository.ProperRepository
import com.proper.enterprise.platform.pay.proper.service.ProperPayService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MvcResult

class ProperControllerTest extends AbstractJPATest {

    @Autowired
    ProperRepository properRepo

    @Autowired
    ProperPayService properPayService

    @Test
    void testPrepay() {
        String outTradeNo = DateUtil.toString(new Date(), "yyyyMMddHHmmssSSS").concat(RandomStringUtils.randomNumeric(15))
        ProperOrderReq properReq = new ProperOrderReq()

        properReq.setOutTradeNo(outTradeNo)
        properReq.setTotalFee("1")
        properReq.setBody("测试预支付")
        MvcResult result = post('/pay/proper/prepay', JSONUtil.toJSON(properReq), HttpStatus.CREATED)

        String obj = result.getResponse().getContentAsString()
        Map<String, Object> doc = (Map<String, Object>)JSONUtil.parse(obj, Object.class)

        assert doc.get("resultCode") == "SUCCESS"

        properReq.setTotalFee("-0.01")
        MvcResult resultErr = post('/pay/proper/prepay', JSONUtil.toJSON(properReq), HttpStatus.CREATED)

        String objErr = resultErr.getResponse().getContentAsString()
        Map<String, Object> docErr = (Map<String, Object>)JSONUtil.parse(objErr, Object.class)

        assert docErr.get("resultCode") == "MONEYERROR"
    }

    @Test
    void testQuery() {
        Map<String, Object> reqMap = new HashMap<>()
        reqMap.put("orderNo", "201512191483583349219")
        reqMap.put("subject", "testProperPay")
        reqMap.put("totalFee", "9.11")
        post("/pay/proper/query", JSONUtil.toJSON(reqMap), HttpStatus.CREATED)

        waitExecutorDone()
        ProperEntity noticeInfo = properPayService.findByOutTradeNo("201512191483583349219")
        ProperEntity businessInfo = properPayService.findByOutTradeNo("001")

        assert noticeInfo.getSubject().equals("testProperPay")
        assert noticeInfo.getTotalFee().equals("9.11")

        assert businessInfo.getSubject().equals("Async notice business")
        assert businessInfo.getTotalFee().equals("0.01")
    }
}
