package com.proper.enterprise.platform.api.pay.enums;

import com.proper.enterprise.platform.core.enums.IntEnum;

public enum PayResType implements IntEnum {

    /**
     * 正常
     */
    SUCCESS(0),

    /**
     * 系统异常
     */
    SYSERROR(1),

    /**
     * 请求对象为空
     */
    REQERROR(2),

    /**
     * 请求订单号为空
     */
    ORDERNUMERROR(3),

    /**
     * 请求金额不正确
     */
    MONEYERROR(4),

    /**
     * 请求支付方式未找到或不正确
     */
    PAYWAYERROR(5),

    /**
     * 请求支付用途为空
     */
    PAYINTENTERROR(6);

    private int code;

    PayResType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static PayResType codeOf(int code) {
        for (PayResType payResType : values()) {
            if (payResType.getCode() == code) {
                return payResType;
            }
        }
        return null;
    }

}
