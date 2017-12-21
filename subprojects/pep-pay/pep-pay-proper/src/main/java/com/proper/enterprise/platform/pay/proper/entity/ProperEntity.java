package com.proper.enterprise.platform.pay.proper.entity;

import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 模拟支付Entity.
 */
@Entity
@Table(name = "PEP_PAY_PROPER_PAYINFO")
@CacheEntity
public class ProperEntity extends BaseEntity {

    /**
     * 通知时间 不可空
     */
    @Column(nullable = false)
    private String notifyTime;

    /**
     * 订单号
     */
    private String tradeNo;

    /**
     * 商户网站唯一订单号 可空
     */
    private String outTradeNo;

    /**
     * 商品名称 可空
     */
    private String subject;

    /**
     * 交易金额 不可空
     */
    @Column(nullable = false)
    private String totalFee;

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }
}
