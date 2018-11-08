package com.proper.enterprise.platform.pay.wechat.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信退款查询响应对象
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WechatRefundQueryRes {

    /**
     * 返回状态码 必填
     */
    @XmlElement(name = "return_code")
    private String returnCode;

    /**
     * 返回信息
     */
    @XmlElement(name = "return_msg")
    private String returnMsg;

    /** 以下字段在return_code为SUCCESS的时候有返回 */

    /**
     * 业务结果 必填
     */
    @XmlElement(name = "result_code")
    private String resultCode;

    /**
     * 错误代码
     */
    @XmlElement(name = "err_code")
    private String errCode;

    /**
     * 错误代码描述
     */
    @XmlElement(name = "err_code_des")
    private String errCodeDes;

    /**
     * 应用APPID 必填
     */
    @XmlElement(name = "appid")
    private String appId;

    /**
     * 商户号 必填
     */
    @XmlElement(name = "mch_id")
    private String mchId;

    /**
     * 设备号
     */
    @XmlElement(name = "device_info")
    private String deviceInfo;

    /**
     * 随机字符串 必填
     */
    @XmlElement(name = "nonce_str")
    private String nonceStr;

    /**
     * 签名 必填
     */
    @XmlElement(name = "sign")
    private String sign;

    /**
     * 微信订单号 必填
     */
    @XmlElement(name = "transaction_id")
    private String transactionId;

    /**
     * 订单总金额 必填
     */
    @XmlElement(name = "total_fee")
    private String totalFee;

    /**
     * 现金支付金额 必填
     */
    @XmlElement(name = "cash_fee")
    private String cashFee;

    /**
     * 退款笔数 必填
     */
    @XmlElement(name = "refund_count")
    private String refundCount;

    /**
     * 商户退款单号 必填
     */
    @XmlElement(name = "out_refund_no_0")
    private String outRefundNo;

    /**
     * 微信退款单号 必填
     */
    @XmlElement(name = "refund_id__0")
    private String refundId;

    /**
     * 退款金额 必填
     */
    @XmlElement(name = "refund_fee_0")
    private String refundFee;

    /**
     * 退款状态 必填
     */
    @XmlElement(name = "refund_status_0")
    private String refundStatus;

    /**
     * 退款入账账户 必填
     */
    @XmlElement(name = "refund_recv_accout_0")
    private String refundRecvAccount;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getCashFee() {
        return cashFee;
    }

    public void setCashFee(String cashFee) {
        this.cashFee = cashFee;
    }

    public String getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(String refundCount) {
        this.refundCount = refundCount;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundRecvAccount() {
        return refundRecvAccount;
    }

    public void setRefundRecvAccount(String refundRecvAccount) {
        this.refundRecvAccount = refundRecvAccount;
    }
}
