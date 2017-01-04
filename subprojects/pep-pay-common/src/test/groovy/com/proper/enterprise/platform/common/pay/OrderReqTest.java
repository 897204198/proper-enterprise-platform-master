package com.proper.enterprise.platform.common.pay;

import com.proper.enterprise.platform.api.pay.model.OrderReq;

public class OrderReqTest implements OrderReq {

    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
