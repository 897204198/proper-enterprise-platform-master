package com.proper.enterprise.platform.api.pay.model;

import java.io.Serializable;

/**
 * 预支付请求对象
 */
public class PrepayReq implements Serializable {

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 商品总金额（以分为单位）
     */
    private String totalFee;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 支付用途
     */
    private String payIntent;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPayIntent() {
        return payIntent;
    }

    public void setPayIntent(String payIntent) {
        this.payIntent = payIntent;
    }
}
