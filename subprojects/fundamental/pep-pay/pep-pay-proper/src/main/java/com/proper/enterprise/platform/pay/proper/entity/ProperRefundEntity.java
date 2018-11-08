package com.proper.enterprise.platform.pay.proper.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 模拟支付退款Entity.
 */
@Entity
@Table(name = "PEP_PAY_PROPER_REFUNDINFO")
@CacheEntity
public class ProperRefundEntity extends BaseEntity {

    /**
     * 返回code 必填
     */
    private String code;

    /**
     * 返回Msg 必填
     */
    private String msg;

    /**
     * 支付宝交易号 必填
     */
    private String tradeNo;

    /**
     * 商户订单号 必填
     */
    private String outTradeNo;

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 退款总金额 必填
     */
    private String refundFee;

    /**
     * 退款支付时间 必填
     */
    private String refundTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

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

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }
}
