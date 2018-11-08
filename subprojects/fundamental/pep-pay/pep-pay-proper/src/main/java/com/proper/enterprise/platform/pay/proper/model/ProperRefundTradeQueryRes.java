package com.proper.enterprise.platform.pay.proper.model;

import java.io.Serializable;

/**
 * 支付宝退款查询
 */
public class ProperRefundTradeQueryRes implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 网关返回码
     */
    private String code;

    /**
     * 网关返回码描述
     */
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
