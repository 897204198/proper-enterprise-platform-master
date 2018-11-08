package com.proper.enterprise.platform.common.pay;

import com.proper.enterprise.platform.api.pay.model.RefundInfoRes;

public class MockRefundInfoRes implements RefundInfoRes {

    private String orderNum;

    private String amout;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getAmout() {
        return amout;
    }

    public void setAmout(String amout) {
        this.amout = amout;
    }
}
