package com.proper.enterprise.platform.pay.ali;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.pay.ali")
public class PayAliProperties {

    /**
     * 支付宝应用窗APPID
     */
    private String appId = "1234567890123456";

    /**
     * 支付宝支付网关
     */
    private String tradeUrl = "https://openapi.alipay.com/gateway.do";

    /**
     * 支付宝查询方法
     */
    private String tradeQueryMethod = "alipay.trade.query";

    /**
     * 支付宝退款方法
     */
    private String tradeRefundMethod = "alipay.trade.refund";

    /**
     * 支付宝退款查询方法
     */
    private String tradeRefundQueryMethod = "alipay.trade.fastpay.refund.query";

    /**
     * 商户PID
     */
    private String partnerId = "1234567890123456";

    /**
     * 商户收款账号
     */
    private String sellerId = "seller@example.org";

    /**
     * 支付宝公钥
     */
    private String aliPublicKey = "publickey";

    /**
     * 支付宝异步通知地址
     */
    private String notifyUrl = "http://foo/bar/pay/ali/noticeAliPayInfo";

    /**
     * 支付宝消息验证地址
     */
    private String httpsVerifyUrl = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

    /**
     * 服务接口名称， 固定值
     */
    private String sucuritypay = "mobile.securitypay.pay";

    /**
     * 支付类型， 固定值
     */
    private String paymentType = "1";

    /**
     * 参数编码
     */
    private String inputCharset = "utf-8";

    /**
     * 设置未付款交易的超时时间
     */
    private String itBPay = "30m";

    /**
     * 商户私钥，pkcs8格式 （支付）
     */
    private String privateKeyForPay = "privatekeyForPay";

    /**
     * 商户私钥，pkcs8格式 （退款）
     */
    private String privateKeyForRefund = "privatekeyForRefund";

    /**
     * 支付宝签名方式 （支付）
     */
    private String signTypeForPay = "RSA";

    /**
     * 支付宝签名方式 （退款）
     */
    private String signTypeForRefund = "RSA2";

    /**
     * 支付签名算法
     */
    private String signAlgoForPay = "SHA1WithRSA";

    /**
     * 退款签名算法
     */
    private String signAlgoForRefund = "SHA256withRsa";

    /**
     * 未知交易状态
     */
    private String noticeTradeStatusUnknown = "UNKONWN";

    /**
     * 交易成功，且可对该交易做操作，如：多级分润、退款等。
     */
    private String noticeTradeStatusTradeSuccess = "TRADE_SUCCESS";

    /**
     * 交易成功且结束，即不可再做任何操作。
     */
    private String noticeTradeStatusTradeFinished = "TRADE_FINISHED";

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTradeUrl() {
        return tradeUrl;
    }

    public void setTradeUrl(String tradeUrl) {
        this.tradeUrl = tradeUrl;
    }

    public String getTradeQueryMethod() {
        return tradeQueryMethod;
    }

    public void setTradeQueryMethod(String tradeQueryMethod) {
        this.tradeQueryMethod = tradeQueryMethod;
    }

    public String getTradeRefundMethod() {
        return tradeRefundMethod;
    }

    public void setTradeRefundMethod(String tradeRefundMethod) {
        this.tradeRefundMethod = tradeRefundMethod;
    }

    public String getTradeRefundQueryMethod() {
        return tradeRefundQueryMethod;
    }

    public void setTradeRefundQueryMethod(String tradeRefundQueryMethod) {
        this.tradeRefundQueryMethod = tradeRefundQueryMethod;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getAliPublicKey() {
        return aliPublicKey;
    }

    public void setAliPublicKey(String aliPublicKey) {
        this.aliPublicKey = aliPublicKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getHttpsVerifyUrl() {
        return httpsVerifyUrl;
    }

    public void setHttpsVerifyUrl(String httpsVerifyUrl) {
        this.httpsVerifyUrl = httpsVerifyUrl;
    }

    public String getSucuritypay() {
        return sucuritypay;
    }

    public void setSucuritypay(String sucuritypay) {
        this.sucuritypay = sucuritypay;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getItBPay() {
        return itBPay;
    }

    public void setItBPay(String itBPay) {
        this.itBPay = itBPay;
    }

    public String getPrivateKeyForPay() {
        return privateKeyForPay;
    }

    public void setPrivateKeyForPay(String privateKeyForPay) {
        this.privateKeyForPay = privateKeyForPay;
    }

    public String getPrivateKeyForRefund() {
        return privateKeyForRefund;
    }

    public void setPrivateKeyForRefund(String privateKeyForRefund) {
        this.privateKeyForRefund = privateKeyForRefund;
    }

    public String getSignTypeForPay() {
        return signTypeForPay;
    }

    public void setSignTypeForPay(String signTypeForPay) {
        this.signTypeForPay = signTypeForPay;
    }

    public String getSignTypeForRefund() {
        return signTypeForRefund;
    }

    public void setSignTypeForRefund(String signTypeForRefund) {
        this.signTypeForRefund = signTypeForRefund;
    }

    public String getSignAlgoForPay() {
        return signAlgoForPay;
    }

    public void setSignAlgoForPay(String signAlgoForPay) {
        this.signAlgoForPay = signAlgoForPay;
    }

    public String getSignAlgoForRefund() {
        return signAlgoForRefund;
    }

    public void setSignAlgoForRefund(String signAlgoForRefund) {
        this.signAlgoForRefund = signAlgoForRefund;
    }

    public String getNoticeTradeStatusUnknown() {
        return noticeTradeStatusUnknown;
    }

    public void setNoticeTradeStatusUnknown(String noticeTradeStatusUnknown) {
        this.noticeTradeStatusUnknown = noticeTradeStatusUnknown;
    }

    public String getNoticeTradeStatusTradeSuccess() {
        return noticeTradeStatusTradeSuccess;
    }

    public void setNoticeTradeStatusTradeSuccess(String noticeTradeStatusTradeSuccess) {
        this.noticeTradeStatusTradeSuccess = noticeTradeStatusTradeSuccess;
    }

    public String getNoticeTradeStatusTradeFinished() {
        return noticeTradeStatusTradeFinished;
    }

    public void setNoticeTradeStatusTradeFinished(String noticeTradeStatusTradeFinished) {
        this.noticeTradeStatusTradeFinished = noticeTradeStatusTradeFinished;
    }
}
