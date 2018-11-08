package com.proper.enterprise.platform.common.pay;

import com.proper.enterprise.platform.api.pay.model.RefundInfoReq;

public class MockRefundInfoReq implements RefundInfoReq {

    private String startDate;

    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
