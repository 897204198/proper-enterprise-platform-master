package com.proper.enterprise.platform.pay.cmb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 已结账单 Body对象
 */
@XmlRootElement(name = "Body")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmbBillBodyRes {
    /**
     * 续传标记(采用多次通讯方式续传时使用)	默认值为’N’，表示没有后续数据包，’Y’表示仍有后续的通讯包
     */
    @XmlElement(name = "QryLopFlg")
    private String qryLopFlg;

    /**
     * 续传包请求数据
     */
    @XmlElement(name = "QryLopBlk")
    private String qryLopBlk;

    /**
     * 续传包请求数据
     */
    @XmlElement(name = "BllRecord")
    private List<CmbBllRecordRes> bllRecords;

    public String getQryLopFlg() {
        return qryLopFlg;
    }

    public void setQryLopFlg(String qryLopFlg) {
        this.qryLopFlg = qryLopFlg;
    }

    public String getQryLopBlk() {
        return qryLopBlk;
    }

    public void setQryLopBlk(String qryLopBlk) {
        this.qryLopBlk = qryLopBlk;
    }

    public List<CmbBllRecordRes> getBllRecords() {
        return bllRecords;
    }

    public void setBllRecords(List<CmbBllRecordRes> bllRecords) {
        this.bllRecords = bllRecords;
    }
}
