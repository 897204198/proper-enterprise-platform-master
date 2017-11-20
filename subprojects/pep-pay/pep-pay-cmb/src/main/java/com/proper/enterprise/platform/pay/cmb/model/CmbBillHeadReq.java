package com.proper.enterprise.platform.pay.cmb.model;

import com.proper.enterprise.platform.core.utils.ConfCenter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 已结对账单 请求Head对象
 */
@XmlRootElement(name = "Head")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmbBillHeadReq implements Serializable {
    private static final long serialVersionUID = -1l;

    /**
     * 4位分行号
     */
    @XmlElement(name = "BranchNo")
    private String branchNo = ConfCenter.get("pay.cmb.branchId");

    /**
     * 6位商户号
     */
    @XmlElement(name = "MerchantNo")
    private String merchantNo = ConfCenter.get("pay.cmb.cono");

    /**
     * 请求发起的时间，精确到毫秒
     */
    @XmlElement(name = "TimeStamp")
    private String timeStamp;

    /**
     * 接口名称
     */
    @XmlElement(name = "Command")
    private String command = "QueryTransact";

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
