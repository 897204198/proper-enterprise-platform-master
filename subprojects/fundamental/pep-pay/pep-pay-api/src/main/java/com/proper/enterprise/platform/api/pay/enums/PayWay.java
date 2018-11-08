package com.proper.enterprise.platform.api.pay.enums;

public enum PayWay {

    /**
     * 支付宝移动支付
     */
    ALI("ali"),

    /**
     * 支付宝网页支付
     */
    ALI_WEB("aliweb"),

    /**
     * 微信支付
     */
    WECHAT("wechat"),

    /**
     * 普日虚拟支付
     */
    PROPER("proper");

    private String text;

    PayWay(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
