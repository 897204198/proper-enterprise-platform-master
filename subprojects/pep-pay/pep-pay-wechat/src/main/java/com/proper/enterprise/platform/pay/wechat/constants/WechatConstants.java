package com.proper.enterprise.platform.pay.wechat.constants;

import com.proper.enterprise.platform.core.utils.ConfCenter;

/**
 * 微信支付常量类
 */
public class WechatConstants {

    /**------------微信证书路径------------------*/
    public static final String WECHAT_PAY_CERT_PATH = ConfCenter.get("pay.wechat.certPath");
    /**------------服务号相关信息------------------*/
    // 服务号的应用号
    public static final String WECHAT_PAY_APPID = ConfCenter.get("pay.wechat.appId");
    // 商户号
    public static final String WECHAT_PAY_MCHID = ConfCenter.get("pay.wechat.mchId");
    // API密钥
    public static final String WECHAT_PAY_API_KEY = ConfCenter.get("pay.wechat.apiKey");
    // 微信异步通知地址
    public static final String WECHAT_PAY_URL_NOTICE = ConfCenter.get("pay.wechat.url.notify");
    /**------------微信支付接口地址------------------*/
    // 微信支付统一接口(POST)
    public static final String WECHAT_PAY_URL_UNIFIED = ConfCenter.get("pay.wechat.url.unified");
    // 微信退款接口(POST)
    public static final String WECHAT_PAY_URL_REFUND = ConfCenter.get("pay.wechat.url.refund");
    // 订单查询接口(POST)
    public static final String WECHAT_PAY_URL_ORDER_QUERY = ConfCenter.get("pay.wechat.url.orderQuery");
    // 退款查询接口(POST)
    public static final String WECHAT_PAY_URL_REFUND_QUERY = ConfCenter.get("pay.wechat.url.refundQuery");
    //微信对账单接口（POST）
    public static final String WECHAT_PAY_URL_BILL = ConfCenter.get("pay.wechat.url.bill");
    /**------------时间信息------------------*/
    // 微信随机字符串
    public static final int WECHAT_PAY_RANDOM_LEN = ConfCenter.getInt("pay.wechat.randomLen", 32);
    // 预支付校验失败
    public static final String WECHAT_PREPAY_VERIFY_ERROR = ConfCenter.get("pay.wechat.prepay.verifyError");
}
