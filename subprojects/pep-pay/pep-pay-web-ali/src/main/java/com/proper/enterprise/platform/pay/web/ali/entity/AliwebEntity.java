package com.proper.enterprise.platform.pay.web.ali.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 支付宝网页异步通知记录表.
 */
@Entity
@Table(name = "PEP_PAY_WEB_ALI_PAYINFO")
@CacheEntity
public class AliwebEntity extends BaseEntity {

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
     * 支付宝分配给开发者的应用Id  不可空
     */
    @Column(nullable = false)
    private String appId;

    /**
     * 编码格式  不可空
     */
    @Column(nullable = false)
    private String charset;

    /**
     * 调用的接口版本，固定为：1.0  不可空
     */
    @Column(nullable = false)
    private String version;

    /**
     * 签名方式 不可空
     */
    @Column(nullable = false)
    private String signType;

    /**
     * 签名 不可空
     */
    @Column(nullable = false, length = 512)
    private String sign;

    /**
     * 支付宝交易凭证号  不可空
     */
    @Column(nullable = false)
    private String tradeNo;

    /**
     * 商户网站唯一订单号 不可空
     */
    @Column(nullable = false)
    private String outTradeNo;

    /**
     * 商户业务ID，主要是退款通知中返回退款申请的流水号 可空
     */
    private String outBizNo;

    /**
     * 买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字 可空
     */
    private String buyerId;

    /**
     * 买家支付宝账号 可空
     */
    private String buyerLogonId;

    /**
     * 卖家支付宝用户号 可空
     */
    private String sellerId;

    /**
     * 卖家支付宝账号 可空
     */
    private String sellerEmail;

    /**
     * 交易状态 可空
     */
    private String tradeStatus;

    /**
     * 交易金额 可空
     */
    private String totalAmount;

    /**
     * 实收金额 可空
     */
    private String receiptAmount;

    /**
     * 开票金额 可空
     */
    private String invoiceAmount;

    /**
     * 付款金额 可空
     */
    private String buyerPayAmount;

    /**
     * 集分宝金额 可空
     */
    private String pointAmount;

    /**
     * 总退款金额 可空
     */
    private String refundFee;

    /**
     * 订单标题 可空
     */
    private String subject;

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
     * 退款时间 可空
     */
    private String gmtRefund;

    /**
     * 交易关闭时间 可空
     */
    private String gmtClose;

    /**
     * 支付成功的各个渠道金额信息 可空
     */
    private String fundBillList;

    /**
     * 回传参数 可空
     */
    private String passbackParams;

    /**
     * 优惠券信息 可空
     */
    private String voucherDetailList;


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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getOutBizNo() {
        return outBizNo;
    }

    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
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

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(String receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getBuyerPayAmount() {
        return buyerPayAmount;
    }

    public void setBuyerPayAmount(String buyerPayAmount) {
        this.buyerPayAmount = buyerPayAmount;
    }

    public String getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(String pointAmount) {
        this.pointAmount = pointAmount;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
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

    public String getFundBillList() {
        return fundBillList;
    }

    public void setFundBillList(String fundBillList) {
        this.fundBillList = fundBillList;
    }

    public String getPassbackParams() {
        return passbackParams;
    }

    public void setPassbackParams(String passbackParams) {
        this.passbackParams = passbackParams;
    }

    public String getVoucherDetailList() {
        return voucherDetailList;
    }

    public void setVoucherDetailList(String voucherDetailList) {
        this.voucherDetailList = voucherDetailList;
    }
}
