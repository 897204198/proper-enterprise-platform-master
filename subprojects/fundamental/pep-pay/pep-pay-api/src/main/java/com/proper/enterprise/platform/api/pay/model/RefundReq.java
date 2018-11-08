package com.proper.enterprise.platform.api.pay.model;

import java.io.Serializable;

/**
 * 退款请求对象
 */
public class RefundReq implements Serializable {

    /**
     * 原订单号(系统内)
     */
    private String outTradeNo;

    /**
     * 退款订单号
     */
    private String outRequestNo;

    /**
     * 退款金额(以分为单位)
     */
    private String refundAmount;

    /**
     * 订单金额(以分为单位)
     */
    private String totalFee;

    /**
     * 退款方式
     */
    private String refundWay;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getRefundWay() {
        return refundWay;
    }

    public void setRefundWay(String refundWay) {
        this.refundWay = refundWay;
    }
}
