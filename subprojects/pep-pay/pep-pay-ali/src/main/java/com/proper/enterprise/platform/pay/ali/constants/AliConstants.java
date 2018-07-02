package com.proper.enterprise.platform.pay.ali.constants;

import com.proper.enterprise.platform.core.utils.ConfCenter;

/**
 * 支付宝常量类
 */
public class AliConstants {

    /**------------------------- 支付宝APP支付信息相关参数 ------------------------------------*/
    /**
     * 商户PID
     */
    public static final String ALI_PAY_PARTNER_ID = ConfCenter.get("pay.ali.partnerId");
    /**
     * 商户收款账号
     */
    public static final String ALI_PAY_SELLER_ID = ConfCenter.get("pay.ali.sellerId");
    /**
     * 支付宝公钥
     */
    public static final String ALI_PAY_RSA_PUBLIC = ConfCenter.get("pay.ali.aliPublicKey");
    /**
     * 支付宝异步通知地址
     */
    public static final String ALI_PAY_NOTICE_URL = ConfCenter.get("pay.ali.notifyurl");
    /**
     * 支付宝消息验证地址
     */
    public static final String ALI_PAY_NOTICE_HTTPS_VERIFY_URL = ConfCenter.get("pay.ali.httpsVerifyUrl");
    /**
     * 服务接口名称， 固定值
     */
    public static final String ALI_PAY_MOBILE_SECURITYPAY_PAY = ConfCenter.get("pay.ali.sucuritypay");
    /**
     * 支付类型， 固定值
     */
    public static final String ALI_PAY_PAYMENT_TYPE = ConfCenter.get("pay.ali.paymentType");
    /**
     * 参数编码
     */
    public static final String ALI_PAY_INPUT_CHARSET = ConfCenter.get("pay.ali.inputCharset");
    /**
     * 设置未付款交易的超时时间
     */
    public static final String ALI_PAY_IT_B_PAY = ConfCenter.get("pay.ali.itBPay");
    /**
     * 支付宝应用窗APPID
     */
    public static final String ALI_PAY_APPID = ConfCenter.get("pay.ali.appId");
    /**
     * 支付宝签名方式 （支付）
     */
    public static final String ALI_PAY_SIGN_TYPE_PAY = ConfCenter.get("pay.ali.signTypeForPay");
    /**
     * 支付宝签名方式 （退款）
     */
    public static final String ALI_PAY_SIGN_TYPE_REFUND = ConfCenter.get("pay.ali.signTypeForRefund");
    /**
     * 商户私钥，pkcs8格式 （支付）
     */
    public static final String ALI_PAY_RSA_PRIVATE_PAY = ConfCenter.get("pay.ali.privateKeyForPay");
    /**
     * 商户私钥，pkcs8格式 （退款）
     */
    public static final String ALI_PAY_RSA_PRIVATE_REFUND = ConfCenter.get("pay.ali.privateKeyForRefund");

    /**------------通知交易状态------------------*/
    /**
     * 未知交易状态
     */
    public static final String ALI_PAY_NOTICE_TARDESTATUS_UNKONWN = ConfCenter.get("pay.ali.notice.trade.status.unknown");
    /**
     * 交易成功，且可对该交易做操作，如：多级分润、退款等。
     */
    public static final String ALI_PAY_NOTICE_TARDESTATUS_TRADE_SUCCESS = ConfCenter.get("pay.ali.notice.trade.status.tradeSuccess");
    /**
     * 交易成功且结束，即不可再做任何操作。
     */
    public static final String ALI_PAY_NOTICE_TARDESTATUS_TRADE_FINISHED = ConfCenter.get("pay.ali.notice.trade.status.tradeFinished");

}
