package com.proper.enterprise.platform.pay.wechat.model;

import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import com.proper.enterprise.platform.pay.wechat.PayWechatProperties;

/**
 * 微信同步调起支付对象
 */
public class WechatPayRes {
    /**
     * 应用ID
     */
    private String appid =  PEPPropertiesLoader.load(PayWechatProperties.class).getAppId();

    /**
     * 商户号
     */
    private String partnerid =  PEPPropertiesLoader.load(PayWechatProperties.class).getMchId();

    /**
     * 预支付交易会话ID
     */
    private String prepayid;

    /**
     * 扩展字段
     */
    private String papackage = "Sign=WXPay";

    /**
     * 随机字符串
     */
    private String noncestr;

    /**
     * 时间戳
     */
    private String timestamp;

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

    public String getPapackage() {
        return papackage;
    }

    public void setPapackage(String papackage) {
        this.papackage = papackage;
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
}
