package com.proper.enterprise.platform.pay.web.ali.model;

import com.proper.enterprise.platform.api.pay.model.PayResultRes;

/**
 * 支付宝网页支付返回前台请求对象.
 */
public class AliwebPayResultRes extends PayResultRes {

    /**
     * 支付宝网页支付请求返回的form
     */
    private String form;

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
