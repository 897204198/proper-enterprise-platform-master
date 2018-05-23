package com.proper.enterprise.platform.pay.ali.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 支付宝支付Entity
 */
@Entity
@Table(name = "PEP_PAY_ALI_PAYINFO")
@CacheEntity
public class AliEntity extends BaseEntity {

    /**
     * 通知时间 不可空
     */
    @Column(nullable = false)
    private String notifyTime;

    /**
     * 通知类型 不可空
     */
    @Column(nullable = false)
    private String notifyType;

    /**
     * 通知校验ID 不可空
     */
    @Column(nullable = false)
    private String notifyId;

    /**
     * 签名方式 不可空
     */
    @Column(nullable = false)
    private String signType;

    /**
     * 签名 不可空
     */
    @Column(nullable = false)
    private String sign;

    /**
     * 商户网站唯一订单号 可空
     */
    private String outTradeNo;

    /**
     * 商品名称 可空
     */
    private String subject;

    /**
     * 支付类型 可空
     */
    private String paymentType;

    /**
     * 支付宝交易号 不可空
     */
    @Column(nullable = false)
    private String tradeNo;

    /**
     * 交易状态 不可空
     */
    @Column(nullable = false)
    private String tradeStatus;

    /**
     * 卖家支付宝用户号 不可空
     */
    @Column(nullable = false)
    private String sellerId;

    /**
     * 卖家支付宝账号 不可空
     */
    @Column(nullable = false)
    private String sellerEmail;

    /**
     * 买家支付宝用户号 不可空
     */
    @Column(nullable = false)
    private String buyerId;

    /**
     * 买家支付宝账号 不可空
     */
    @Column(nullable = false)
    private String buyerEmail;

    /**
     * 交易金额 不可空
     */
    @Column(nullable = false)
    private String totalFee;

    /**
     * 购买数量 可空
     */
    private String quantity;

    /**
     * 商品单价 可空
     */
    private String price;

    /**
     * 商品描述 可空
     */
    private String body;

    /**
     * 交易创建时间 可空
     */
    private String gmtCreate;

    /**
     * 交易付款时间 可空
     */
    private String gmtPayment;

    /**
     * 是否调整总价 可空
     */
    private String isTotalFeeAdjust;

    /**
     * 是否使用红包买家 可空
     */
    private String useCoupon;

    /**
     * 折扣 可空
     */
    private String discount;

    /**
     * 退款状态 可空
     */
    private String refundStatus;

    /**
     * 退款时间 可空
     */
    private String gmtRefund;

    /**
     * 交易关闭时间 可空
     */
    private String gmtClose;

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtPayment() {
        return gmtPayment;
    }

    public void setGmtPayment(String gmtPayment) {
        this.gmtPayment = gmtPayment;
    }

    public String getIsTotalFeeAdjust() {
        return isTotalFeeAdjust;
    }

    public void setIsTotalFeeAdjust(String isTotalFeeAdjust) {
        this.isTotalFeeAdjust = isTotalFeeAdjust;
    }

    public String getUseCoupon() {
        return useCoupon;
    }

    public void setUseCoupon(String useCoupon) {
        this.useCoupon = useCoupon;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getGmtRefund() {
        return gmtRefund;
    }

    public void setGmtRefund(String gmtRefund) {
        this.gmtRefund = gmtRefund;
    }

    public String getGmtClose() {
        return gmtClose;
    }

    public void setGmtClose(String gmtClose) {
        this.gmtClose = gmtClose;
    }
}
