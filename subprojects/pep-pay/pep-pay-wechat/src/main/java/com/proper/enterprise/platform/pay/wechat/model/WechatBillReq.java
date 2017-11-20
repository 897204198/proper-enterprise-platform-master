package com.proper.enterprise.platform.pay.wechat.model;

import com.proper.enterprise.platform.pay.wechat.constants.WechatConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信获取对账单_XML_Model
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WechatBillReq {
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
     * 设备号
     */
    @XmlElement(name = "device_info")
    private String deviceInfo = "WEB";

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

    /**
     * 对账单日期
     */
    @XmlElement(name = "bill_date")
    private String billDate;

    /**
     * 账单类型
       SUCCESS，返回当日成功支付的订单
       REFUND，返回当日退款订单
       RECHARGE_REFUND，返回当日充值退款订单（相比其他对账单多一栏“返还手续费”）
     */
    @XmlElement(name = "bill_type")
    private String billType = "ALL";

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

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }
}
