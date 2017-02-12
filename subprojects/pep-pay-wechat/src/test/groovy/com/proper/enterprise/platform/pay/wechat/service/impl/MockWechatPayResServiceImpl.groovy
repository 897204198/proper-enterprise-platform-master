package com.proper.enterprise.platform.pay.wechat.service.impl

import com.proper.enterprise.platform.pay.wechat.model.WechatOrderRes
import com.proper.enterprise.platform.pay.wechat.model.WechatPayQueryRes
import com.proper.enterprise.platform.pay.wechat.model.WechatRefundQueryRes
import com.proper.enterprise.platform.pay.wechat.model.WechatRefundRes
import com.proper.enterprise.platform.pay.wechat.service.WechatPayResService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
class MockWechatPayResServiceImpl extends WechatPayResServiceImpl implements WechatPayResService {

    /**
     * 向微信服务器发送http请求
     *
     * @param url 请求地址
     * @param beanId 实例Bean
     * @param requestXML 请求报文
     * @param isHttpsRequest true : http请求 ; false : https请求
     * @param <T> 泛型
     * @return 请求结果
     * @throws Exception
     */
    @Override
    public <T> T getWechatApiRes(String url, String beanId, String requestXML, boolean isHttpsRequest) throws Exception {
        // 预支付
        if(beanId.equals("unmarshallWechatOrderRes")) {
            WechatOrderRes orderRes = new WechatOrderRes()
            if(requestXML.contains("12345678901234567890")) {
                orderRes.setResultCode("SUCCESS")
                orderRes.setReturnCode("SUCCESS")
                orderRes.setPrepayId("wx201701102105480e38692c210617631918")
            } else {
                orderRes.setResultCode("FAIL")
                orderRes.setReturnCode("FAIL")
            }
            return (T)orderRes

            // 查询订单
        } else if(beanId.equals("unmarshallWechatPayQueryRes")) {
            WechatPayQueryRes queryRes = new WechatPayQueryRes()
            queryRes.setReturnCode("SUCCESS")
            queryRes.setReturnMsg("OK")
            queryRes.setResultCode("SUCCESS")
            queryRes.setTradeState("SUCCESS")
            queryRes.setOutTradeNo("201512191484099315532")
            queryRes.setTotalFee("5")
            queryRes.setCashFee("5")
            return (T)queryRes

            // 退款
        } else if(beanId.equals("unmarshallWechatRefundRes")) {
            WechatRefundRes refundRes = new WechatRefundRes()
            refundRes.setReturnCode("SUCCESS")
            refundRes.setReturnMsg("OK")
            refundRes.setResultCode("SUCCESS")
            refundRes.setOutTradeNo("201512191484099315532")
            refundRes.setOutRefundNo("20151219148409931553201")
            refundRes.setTotalFee("5")
            refundRes.setRefundFee("1")
            return (T)refundRes

            // 退款查询
        } else if(beanId.equals("unmarshallWechatRefundQueryRes")) {
            WechatRefundQueryRes refundQueryRes = new WechatRefundQueryRes()
            refundQueryRes.setReturnCode("SUCCESS")
            refundQueryRes.setReturnMsg("OK")
            refundQueryRes.setResultCode("SUCCESS")
            refundQueryRes.setOutRefundNo("20151219148409931553201")
            refundQueryRes.setTotalFee("5")
            refundQueryRes.setRefundFee("1")
            refundQueryRes.setRefundStatus("SUCCESS")
            return (T)refundQueryRes
        }
    }
}
