package com.proper.enterprise.platform.api.pay;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.pay.api.way")
public class PayApiWayProperties {

    /**
     * 支付名称ali
     */
    private String ali = "ali";

    /**
     * 支付名称wechat
     */
    private String wechat = "wechat";

    /**
     * 支付名称cmb
     */
    private String cmb = "cmb";

    /**
     * 支付名称proper
     */
    private String proper = "proper";

    /**
     * 支付名称aliweb
     */
    private String aliWeb = "aliweb";

    public String getAli() {
        return ali;
    }

    public void setAli(String ali) {
        this.ali = ali;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getCmb() {
        return cmb;
    }

    public void setCmb(String cmb) {
        this.cmb = cmb;
    }

    public String getProper() {
        return proper;
    }

    public void setProper(String proper) {
        this.proper = proper;
    }

    public String getAliWeb() {
        return aliWeb;
    }

    public void setAliWeb(String aliWeb) {
        this.aliWeb = aliWeb;
    }
}
