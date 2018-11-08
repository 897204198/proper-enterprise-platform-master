package com.proper.enterprise.platform.pay.web.ali.model;

import com.proper.enterprise.platform.api.pay.model.OrderReq;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import com.proper.enterprise.platform.pay.web.ali.PayWebAliProperties;

/**
 * 支付宝网页支付接收前台请求支付信息参数.
 */
public class AliwebOrderReq implements OrderReq {

    /**
     * 商户网站唯一订单号
     */
    private String outTradeNo;

    /**
     * 商品名称
     */
    private String subject;

    /**
     * 商品详情
     */
    private String body;

    /**
     * 商品总金额
     */
    private String totalFee;

    /**
     * 服务器异步通知页面路径
     */
    private String notifyUrl;

    /**
     * 设置未付款交易的超时时间
     */
    private String timeoutExpress = PEPPropertiesLoader.load(PayWebAliProperties.class)
        .getPayWebAliTimeoutExpress();

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
    }
}
