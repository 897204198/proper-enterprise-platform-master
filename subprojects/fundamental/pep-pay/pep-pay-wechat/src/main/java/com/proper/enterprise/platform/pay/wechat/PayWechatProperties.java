package com.proper.enterprise.platform.pay.wechat;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.pay.wechat")
public class PayWechatProperties {

    /**
     * 微信随机字符串
     */
    private int randomLen = 16;

    /**
     * MD5最大长度
     */
    private int abbreviateMaxWidth = 100;
    /**
     * API密钥
     */
    private String apiKey = "apikey";
    /**
     * 服务号的应用号
     */
    private String appId = "appid";
    /**
     * 商户号
     */
    private String mchId = "1234567890";
    /**
     * 微信支付统一接口(POST)
     */
    private String urlUnified = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * 微信退款接口(POST)
     */
    private String urlRefund = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    /**
     * 微信异步通知地址
     */
    private String urlNotify = "http://foo/bar/pay/w/noticeWPayInfo";
    /**
     * 订单查询接口(POST)
     */
    private String urlOrderQuery = "https://api.mch.weixin.qq.com/pay/orderquery";
    /**
     * 退款查询接口(POST)
     */
    private String urlRrefundQuery = "https://api.mch.weixin.qq.com/pay/refundquery";
    /**
     * 微信对账单接口（POST）
     */
    private String urlBill = "https://api.mch.weixin.qq.com/pay/downloadbill";
    /**
     * 微信证书路径
     */
    private String certPath = "weixincert/cert.p12";
    /**
     * 预支付校验失败
     */
    private String prepayVerifyError = "W_PREPAY_VERIFY_ERROR";

    /**
     * 终端IP地址
     */
    private String spbillCreateIp = "localhost";

    public int getRandomLen() {
        return randomLen;
    }

    public void setRandomLen(int randomLen) {
        this.randomLen = randomLen;
    }

    public int getAbbreviateMaxWidth() {
        return abbreviateMaxWidth;
    }

    public void setAbbreviateMaxWidth(int abbreviateMaxWidth) {
        this.abbreviateMaxWidth = abbreviateMaxWidth;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
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

    public String getUrlUnified() {
        return urlUnified;
    }

    public void setUrlUnified(String urlUnified) {
        this.urlUnified = urlUnified;
    }

    public String getUrlRefund() {
        return urlRefund;
    }

    public void setUrlRefund(String urlRefund) {
        this.urlRefund = urlRefund;
    }

    public String getUrlNotify() {
        return urlNotify;
    }

    public void setUrlNotify(String urlNotify) {
        this.urlNotify = urlNotify;
    }

    public String getUrlOrderQuery() {
        return urlOrderQuery;
    }

    public void setUrlOrderQuery(String urlOrderQuery) {
        this.urlOrderQuery = urlOrderQuery;
    }

    public String getUrlRrefundQuery() {
        return urlRrefundQuery;
    }

    public void setUrlRrefundQuery(String urlRrefundQuery) {
        this.urlRrefundQuery = urlRrefundQuery;
    }

    public String getUrlBill() {
        return urlBill;
    }

    public void setUrlBill(String urlBill) {
        this.urlBill = urlBill;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getPrepayVerifyError() {
        return prepayVerifyError;
    }

    public void setPrepayVerifyError(String prepayVerifyError) {
        this.prepayVerifyError = prepayVerifyError;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }
}
