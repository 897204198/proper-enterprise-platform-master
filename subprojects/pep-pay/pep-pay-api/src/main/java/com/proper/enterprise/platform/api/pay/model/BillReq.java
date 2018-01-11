package com.proper.enterprise.platform.api.pay.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 对账单请求时间
 */
public class BillReq implements Serializable {

    /**
     * 对账单日期
     */
    private Date date;

    /**
     * 数据类型
     */
    private String billType;

    public Date getDate() {
        return date == null ? null : (Date) date.clone();
    }

    public void setDate(Date date) {
        this.date = date == null ? null : (Date) date.clone();
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }
}
