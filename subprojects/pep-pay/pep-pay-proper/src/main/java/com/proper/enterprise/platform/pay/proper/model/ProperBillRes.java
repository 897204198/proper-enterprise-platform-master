package com.proper.enterprise.platform.pay.proper.model;

import java.io.Serializable;

/**
 * 模拟支付账单结果对象
 */
public class ProperBillRes implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     *  账单下载路径
     */
    private String downLoadUrl;

    /**
     *  网关返回码
     */
    private String code;

    /**
     *  网关返回描述
     */
    private String msg;

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
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
