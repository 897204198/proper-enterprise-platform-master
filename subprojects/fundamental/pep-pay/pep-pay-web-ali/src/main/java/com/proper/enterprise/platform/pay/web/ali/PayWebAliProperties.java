package com.proper.enterprise.platform.pay.web.ali;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.pay.web.ali")
public class PayWebAliProperties {

    /**
     * 支付宝手机网页支付应用窗APPID
     */
    private String payWebAliAppId = "appId";
    /**
     * 支付宝手机网页支付网关URL
     */
    private String payWebAliTradeUrl = "https://openapi.alipay.com/gateway.do";

    /**
     * 支付宝手机网页支付私钥
     */
    private String payWebAliPrivateKey = "privatekey";

    /**
     * 参数返回格式，只支持json
     */
    private String payWebAliFormat = "json";

    /**
     * 支付宝公钥,由支付宝生成
     */
    private String payWebAlialiPublicKey = "publickey";

    /**
     * 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
     */
    private String payWebAliSignType = "RSA2";

    /**
     * 销售产品码，商家和支付宝签约的产品码
     */
    private String payWebAliProductCode = "QUICK_WAP_PAY";

    /**
     * 异步通知地址
     */
    private String payWebAliNotifyUrl = "http://foo/bar/pay/aliweb/noticeAliwebPayInfo";

    /**
     * 同步通知地址
     */
    private String payWebAliReturnUrl = "http://foo/bar/pay/aliweb/noticeAliwebReturnUrl";

    /**
     * 默认交易超时时间
     */
    private String payWebAliTimeoutExpress = "30m";
    /**
     * 未知交易状态
     */
    private String payWebAliNoticeTradeStatusUnknown = "UNKONWN";

    /**
     * 交易成功，且可对该交易做操作，如：多级分润、退款等。
     */
    private String payWebAliNoticeTradeStatusTradeSuccess = "TRADE_SUCCESS";
    /**
     * 交易成功且结束，即不可再做任何操作。
     */
    private String payWebAliNoticeTradeStatusTradeFinished = "TRADE_FINISHED";

    public String getPayWebAliAppId() {
        return payWebAliAppId;
    }

    public void setPayWebAliAppId(String payWebAliAppId) {
        this.payWebAliAppId = payWebAliAppId;
    }

    public String getPayWebAliTradeUrl() {
        return payWebAliTradeUrl;
    }

    public void setPayWebAliTradeUrl(String payWebAliTradeUrl) {
        this.payWebAliTradeUrl = payWebAliTradeUrl;
    }

    public String getPayWebAliPrivateKey() {
        return payWebAliPrivateKey;
    }

    public void setPayWebAliPrivateKey(String payWebAliPrivateKey) {
        this.payWebAliPrivateKey = payWebAliPrivateKey;
    }

    public String getPayWebAliFormat() {
        return payWebAliFormat;
    }

    public void setPayWebAliFormat(String payWebAliFormat) {
        this.payWebAliFormat = payWebAliFormat;
    }

    public String getPayWebAlialiPublicKey() {
        return payWebAlialiPublicKey;
    }

    public void setPayWebAlialiPublicKey(String payWebAlialiPublicKey) {
        this.payWebAlialiPublicKey = payWebAlialiPublicKey;
    }

    public String getPayWebAliSignType() {
        return payWebAliSignType;
    }

    public void setPayWebAliSignType(String payWebAliSignType) {
        this.payWebAliSignType = payWebAliSignType;
    }

    public String getPayWebAliProductCode() {
        return payWebAliProductCode;
    }

    public void setPayWebAliProductCode(String payWebAliProductCode) {
        this.payWebAliProductCode = payWebAliProductCode;
    }

    public String getPayWebAliNotifyUrl() {
        return payWebAliNotifyUrl;
    }

    public void setPayWebAliNotifyUrl(String payWebAliNotifyUrl) {
        this.payWebAliNotifyUrl = payWebAliNotifyUrl;
    }

    public String getPayWebAliReturnUrl() {
        return payWebAliReturnUrl;
    }

    public void setPayWebAliReturnUrl(String payWebAliReturnUrl) {
        this.payWebAliReturnUrl = payWebAliReturnUrl;
    }

    public String getPayWebAliTimeoutExpress() {
        return payWebAliTimeoutExpress;
    }

    public void setPayWebAliTimeoutExpress(String payWebAliTimeoutExpress) {
        this.payWebAliTimeoutExpress = payWebAliTimeoutExpress;
    }

    public String getPayWebAliNoticeTradeStatusUnknown() {
        return payWebAliNoticeTradeStatusUnknown;
    }

    public void setPayWebAliNoticeTradeStatusUnknown(String payWebAliNoticeTradeStatusUnknown) {
        this.payWebAliNoticeTradeStatusUnknown = payWebAliNoticeTradeStatusUnknown;
    }

    public String getPayWebAliNoticeTradeStatusTradeSuccess() {
        return payWebAliNoticeTradeStatusTradeSuccess;
    }

    public void setPayWebAliNoticeTradeStatusTradeSuccess(String payWebAliNoticeTradeStatusTradeSuccess) {
        this.payWebAliNoticeTradeStatusTradeSuccess = payWebAliNoticeTradeStatusTradeSuccess;
    }

    public String getPayWebAliNoticeTradeStatusTradeFinished() {
        return payWebAliNoticeTradeStatusTradeFinished;
    }

    public void setPayWebAliNoticeTradeStatusTradeFinished(String payWebAliNoticeTradeStatusTradeFinished) {
        this.payWebAliNoticeTradeStatusTradeFinished = payWebAliNoticeTradeStatusTradeFinished;
    }
}
