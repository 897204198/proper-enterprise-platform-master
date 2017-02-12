package com.proper.enterprise.platform.pay.ali.model;

/**
 * 支付宝退款请求对象
 */
public class AliRefundReq {
    /**
     * 原订单号
     */
    private String outTradeNo;

    /**
     * 退款订单单号
     */
    private String refundNo;

    /**
     * 退款金额(以元为单位)
     */
    private String amount;

    /**
     * 退款理由
     */
    private String refundReason;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
}
