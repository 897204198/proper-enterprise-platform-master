package com.proper.enterprise.platform.pay.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;

/**
 * 支付统一返回对象
 */
public class WechatPayResultRes extends PayResultRes {

    /**
     * 微信支付应用ID
     */
    private String appid;

    /**
     * 微信支付商户号
     */
    private String partnerid;

    /**
     * 微信支付预支付交易会话ID
     */
    private String prepayid;

    /**
     * 微信支付扩展字段
     */
    @JsonProperty("package")
    private String wxpackage;

    /**
     * 微信支付随机字符串
     */
    private String noncestr;

    /**
     * 微信支付时间戳
     */
    private String timestamp;

    /**
     * 签名
     */
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getWxpackage() {
        return wxpackage;
    }

    public void setWxpackage(String wxpackage) {
        this.wxpackage = wxpackage;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
