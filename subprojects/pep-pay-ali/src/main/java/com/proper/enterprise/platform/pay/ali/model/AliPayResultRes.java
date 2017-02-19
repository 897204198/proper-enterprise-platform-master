package com.proper.enterprise.platform.pay.ali.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;

/**
 * 支付统一返回对象
 */
public class AliPayResultRes extends PayResultRes {

    /**
     * 支付宝支付信息
     */
    @JsonProperty("pay_info")
    private String payInfo;

    /**
     * 签名
     */
    private String sign;

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
