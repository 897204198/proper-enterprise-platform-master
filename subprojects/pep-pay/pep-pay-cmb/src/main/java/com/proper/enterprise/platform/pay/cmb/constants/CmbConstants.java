package com.proper.enterprise.platform.pay.cmb.constants;

import com.proper.enterprise.platform.core.utils.ConfCenter;

/**
 * 一网通常量类
 */
public class CmbConstants {

    /**------------public.key路径-----------*/
    public static final String CMB_PAY_KEYPATH = ConfCenter.get("pay.cmb.keyPath");

    /**------------基本信息------------------*/
    // 一网通公钥
    public static final String CMB_PAY_PUBLICKEY = ConfCenter.get("pay.cmb.publickey");
    // 一网通用户秘钥
    public static final String CMB_PAY_CMBKEY = ConfCenter.get("pay.cmb.cmbKey");
    // 支付商户开户分行号
    public static final String CMB_PAY_BRANCHID = ConfCenter.get("pay.cmb.branchId");
    // 支付商户号，6位长数字，由银行在商户开户时确定；//收单商户号
    public static final String CMB_PAY_CONO = ConfCenter.get("pay.cmb.cono");
    // 一网通支付请求地址
    public static final String CMB_PAY_PAYMENT_URL = ConfCenter.get("pay.cmb.payMentUrl");
    // 商户请求通用接口
    public static final String CMB_PAY_DIRECT_REQUEST_X = ConfCenter.get("pay.cmb.directRequestX");
    // 一网通协议异步通知地址
    public static final String CMB_PAY_PROTOCOL_URL = ConfCenter.get("pay.cmb.protocolUrl");
    // 一网通异步通知地址
    public static final String CMB_PAY_NOTICE_URL = ConfCenter.get("pay.cmb.merchantUrl");

    /**------------用户信息相关------------------*/
    // 用户已签约
    public static final String CMB_PAY_PROTOCOL_SIGNED = ConfCenter.get("pay.cmb.protocol.signed");
    // 用户未签约
    public static final String CMB_PAY_PROTOCOL_UNSIGNED = ConfCenter.get("pay.cmb.protocol.unSigned");
    // 一网通协议成功状态
    public static final String CMB_PAY_PROTOCOL_SUCCESS = ConfCenter.get("pay.cmb.signSuccess");
    //  用户信息错误
    public static final String CMB_PAY_ERROR_USERID = ConfCenter.get("pay.cmb.err.userId");
    //  订单号错误
    public static final String CMB_PAY_ERROR_BILLNO_ERROR = ConfCenter.get("pay.cmb.err.billNo.error");

    /**------------日期格式------------------*/
    // 日期格式:yyyyMMddHHmmss
    public static final String CMB_PAY_DATE_FORMAT_YYYYMMDDHHMMSS = ConfCenter.get("pay.cmb.date.format.yyyyMMddHHmmss");
    // 日期格式:yyyyMMddHHmmssSSS
    public static final String CMB_PAY_DATE_FORMAT_YYYYMMDDHHMMSSSSS = ConfCenter.get("pay.cmb.date.format.yyyyMMddHHmmssSSS");
    // 日期格式:yyyyMMdd
    public static final String CMB_PAY_DATE_FORMAT_YYYYMMDD = ConfCenter.get("pay.cmb.date.format.yyyyMMdd");
    // xml头
    public static final String CMB_PAY_XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

}
