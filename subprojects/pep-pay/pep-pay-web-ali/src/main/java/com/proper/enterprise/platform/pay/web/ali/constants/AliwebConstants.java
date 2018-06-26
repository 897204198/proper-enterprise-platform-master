package com.proper.enterprise.platform.pay.web.ali.constants;

import com.proper.enterprise.platform.core.utils.ConfCenter;

/**
 * 支付宝网页支付常量类.
 */
public class AliwebConstants {

    /**------------------------- 支付宝APP网页支付信息相关参数 ------------------------------------*/
    /**
     * 支付宝手机网页支付应用窗APPID
     */
    public static final String ALI_WEBPAY_APPID = ConfCenter.get("payweb.ali.appId");

    /**
     * 支付宝手机网页支付网关URL
     */
    public static final String ALI_WEBPAY_URL = ConfCenter.get("payweb.ali.tradeUrl");

    /**
     * 支付宝手机网页支付私钥
     */
    public static final String ALI_WEBPAY_PRIVATEKEY = ConfCenter.get("payweb.ali.privateKey");

    /**
     * 参数返回格式，只支持json
     */
    public static final String ALI_WEBPAY_FORMAT = ConfCenter.get("payweb.ali.format");

    /**
     * 请求和签名使用的字符编码格式，支持GBK和UTF-8
     */
    public static final String ALI_WEBPAY_CHARSET = ConfCenter.get("payweb.ali.inputCharset");

    /**
     * 支付宝公钥,由支付宝生成
     */
    public static final String ALI_WEBPAY_PUBLICKEY = ConfCenter.get("payweb.ali.aliPublicKey");

    /**
     * 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
     */
    public static final String ALI_WEBPAY_SIGNTYPE = ConfCenter.get("payweb.ali.signType");

    /**
     * 销售产品码，商家和支付宝签约的产品码
     */
    public static final String ALI_WEBPAY_PRODUCT_CODE = ConfCenter.get("payweb.ali.productCode");

    /**
     * 异步通知地址
     */
    public static final String ALI_WEBPAY_NOTIFY_URL = ConfCenter.get("payweb.ali.notifyurl");

    /**
     * 同步通知地址
     */
    public static final String ALI_WEBPAY_RETURN_URL = ConfCenter.get("payweb.ali.returnurl");

    /**
     * 默认交易超时时间
     */
    public static final String ALI_WEBPAY_TIMEOUT_EXPRESS = ConfCenter.get("payweb.ali.timeoutExpress");

    /**------------通知交易状态------------------*/
    /**
     * 未知交易状态
     */
    public static final String ALI_WEBPAY_NOTICE_TARDESTATUS_UNKONWN = ConfCenter.get("payweb.ali.notice.trade.status.unknown");

    /**
     * 交易成功，且可对该交易做操作，如：多级分润、退款等。
     */
    public static final String ALI_WEBPAY_NOTICE_TARDESTATUS_TRADE_SUCCESS = ConfCenter.get("payweb.ali.notice.trade.status.tradeSuccess");

    /**
     * 交易成功且结束，即不可再做任何操作。
     */
    public static final String ALI_WEBPAY_NOTICE_TARDESTATUS_TRADE_FINISHED = ConfCenter.get("payweb.ali.notice.trade.status.tradeFinished");

}
