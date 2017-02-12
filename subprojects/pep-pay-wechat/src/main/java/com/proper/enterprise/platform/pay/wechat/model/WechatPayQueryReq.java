package com.proper.enterprise.platform.pay.wechat.model;

import com.proper.enterprise.platform.pay.wechat.constants.WechatConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信订单查询请求对象
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WechatPayQueryReq {
    /**
     * 应用ID 必填
     */
    @XmlElement(name = "appid")
    private String appid = WechatConstants.WECHAT_PAY_APPID;

    /**
     * 商户号 必填
     */
    @XmlElement(name = "mch_id")
    private String mchId = WechatConstants.WECHAT_PAY_MCHID;

    /**
     * 微信订单号 (与商户订单号二选一)
     */
    @XmlElement(name = "transaction_id")
    private String transactionId;

    /**
     * 商户订单号 (与商户订单号二选一)
     */
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;


    /**
     * 商户退款单号 (四选一)
     */
    @XmlElement(name = "out_refund_no")
    private String outRefundNo;

    /**
     * 微信退款单号 (四选一)
     */
    @XmlElement(name = "refund_id")
    private String refundId;


    /**
     * 随机字符串 必填
     */
    @XmlElement(name = "nonce_str")
    private String nonceStr;

    /**
     * 签名
     */
    @XmlElement(name = "sign")
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
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
}
