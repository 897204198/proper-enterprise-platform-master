package com.proper.enterprise.platform.pay.cmb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;

/**
 * 一网通返回对象
 */
public class CmbPayResultRes extends PayResultRes {

    /**
     * 一网通支付信息
     */
    @JsonProperty("pay_info")
    private String payInfo;

    /**
     * 一网通订单号
     */
    private String cmbBillNo;

    /**
     * 一网通订单日期
     */
    private String cmbDate;

    /**
     * 一网通订单金额
     */
    private String amout;

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getCmbBillNo() {
        return cmbBillNo;
    }

    public void setCmbBillNo(String cmbBillNo) {
        this.cmbBillNo = cmbBillNo;
    }

    public String getCmbDate() {
        return cmbDate;
    }

    public void setCmbDate(String cmbDate) {
        this.cmbDate = cmbDate;
    }

    public String getAmout() {
        return amout;
    }

    public void setAmout(String amout) {
        this.amout = amout;
    }
}
