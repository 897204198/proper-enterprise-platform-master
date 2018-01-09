package com.proper.enterprise.platform.pay.proper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;

/**
 * 支付统一返回对象.
 */
public class ProperPayResultRes extends PayResultRes {

    /**
     * 模拟支付信息
     */
    @JsonProperty("pay_info")
    private String payInfo;

    /**
     * 订单号
     */
    private String orderNo;

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
