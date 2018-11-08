package com.proper.enterprise.platform.api.pay;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.pay.api.error")
public class PayApiErrorProperties {

    /**
     * 系统异常Message
     */
    private String system = "SYSTEM_ERROR";

    /**
     * 支付request异常
     */
    private String payReq = "REQ_ERROR";

    /**
     * 订单异常
     */
    private String order = "ORDER_ERROR";

    /**
     * 金额异常
     */
    private String money = "MONEY_ERROR";

    /**
     * 支付网关异常
     */
    private String payWay = "PAYWAY_ERROR";

    /**
     * 支付意图异常
     */
    private String payIntent = "PAYINTENT_ERROR";

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getPayReq() {
        return payReq;
    }

    public void setPayReq(String payReq) {
        this.payReq = payReq;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPayIntent() {
        return payIntent;
    }

    public void setPayIntent(String payIntent) {
        this.payIntent = payIntent;
    }
}
