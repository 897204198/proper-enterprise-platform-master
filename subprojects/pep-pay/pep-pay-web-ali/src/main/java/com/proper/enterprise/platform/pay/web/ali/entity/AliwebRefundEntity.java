package com.proper.enterprise.platform.pay.web.ali.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 支付宝网页支付退款Entity.
 */
@Entity
@Table(name = "PEP_WEBPAY_ALI_REFUNDINFO")
@CacheEntity
public class AliwebRefundEntity extends BaseEntity {

    //******************公共响应参数************************
    /**
     * 网关返回码 必填
     */
    private String code;

    /**
     * 网关返回码描述 必填
     */
    private String msg;

    /**
     * 业务返回码 可空
     */
    private String subCode;

    /**
     * 业务返回码描述 可空
     */
    private String subMsg;

    /**
     * 网关返回码 必填
     */
    private String sign;

    //******************响应参数************************
    /**
     * 支付宝交易号 必填
     */
    private String tradeNo;

    /**
     * 商户订单号 必填
     */
    private String outTradeNo;

    /**
     * 买家支付宝用户号，该参数已废弃，请不要使用 必填
     */
    private String openId;

    /**
     * 用户的登录id 必填
     */
    private String buyerLogonId;

    /**
     * 本次退款是否发生了资金变化 必填
     */
    private String fundChange;

    /**
     * 退款总金额 必填
     */
    private String refundFee;

    /**
     * 退款支付时间 必填
     */
    private String gmtRefundPay;

    /**
     * 退款使用的资金渠道 可空
     */
    private String refundDetailItem;

    /**
     * 交易在支付时候的门店名称 可空
     */
    private String storeName;

    /**
     * 买家在支付宝的用户id  必填
     */
    private String buyerUserId;

    /**
     * 退款单号
     */
    private String refundNo;

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

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getFundChange() {
        return fundChange;
    }

    public void setFundChange(String fundChange) {
        this.fundChange = fundChange;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getGmtRefundPay() {
        return gmtRefundPay;
    }

    public void setGmtRefundPay(String gmtRefundPay) {
        this.gmtRefundPay = gmtRefundPay;
    }

    public String getRefundDetailItem() {
        return refundDetailItem;
    }

    public void setRefundDetailItem(String refundDetailItem) {
        this.refundDetailItem = refundDetailItem;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(String buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }
}
