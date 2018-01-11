package com.proper.enterprise.platform.pay.ali.model;

import java.io.Serializable;

/**
 * 支付宝账单结果对象
 */
public class AliBillRes implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     *  账单下载路径
     */
    private String billDownloadUrl;

    /**
     *  网关返回码
     */
    private String code;

    /**
     *  网关返回描述
     */
    private String msg;

    public String getBillDownloadUrl() {
        return billDownloadUrl;
    }

    public void setBillDownloadUrl(String billDownloadUrl) {
        this.billDownloadUrl = billDownloadUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
