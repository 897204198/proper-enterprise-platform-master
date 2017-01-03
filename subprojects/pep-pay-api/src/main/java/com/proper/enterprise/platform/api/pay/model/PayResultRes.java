package com.proper.enterprise.platform.api.pay.model;

import com.proper.enterprise.platform.api.pay.enums.PayResType;

/**
 * 支付统一返回对象
 */
public class PayResultRes {

    /**
     * 结果
     */
    private PayResType resultCode;

    /**
     * 消息
     */
    private String resultMsg;

    public PayResType getResultCode() {
        return resultCode;
    }

    public void setResultCode(PayResType resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
