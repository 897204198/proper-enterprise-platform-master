package com.proper.enterprise.platform.api.pay.constants;

import com.proper.enterprise.platform.core.utils.ConfCenter;

/**
 * 支付常量类
 */
public class PayConstants {
    /**------------常量信息------------------*/
    public final static String APP_SYSTEM_ERR = ConfCenter.get("pay.app.system.error");
    public final static String APP_PAY_REQ_ERR = ConfCenter.get("pay.app.pay.req.error");
    public final static String APP_PAY_ORDERNO_ERR = ConfCenter.get("pay.app.pay.orderno.error");
    public final static String APP_PAY_MONEY_ERR = ConfCenter.get("pay.app.pay.money.error");
    public final static String APP_PAY_PAYWAY_ERR = ConfCenter.get("pay.app.pay.payway.error");
    public final static String APP_PAY_PAYINTENT_ERR = ConfCenter.get("pay.app.pay.payintent.error");
}
