package com.proper.enterprise.platform.pay.cmb.model;

import com.proper.enterprise.platform.core.utils.ConfCenter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 对账单 已结账单 请求Body对象
 */
@XmlRootElement(name = "Body")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmbBillBodyReq implements Serializable {
    private static final long serialVersionUID = -1L;
    /**
     * 查询开始时间
     */
    @XmlElement(name = "BeginDate")
    private String beginDate;

    /**
     * 查询结束时间
     */
    @XmlElement(name = "EndDate")
    private String endDate;

    /**
     * 条数（不超3位数）
     */
    @XmlElement(name = "Count")
    private String count = "999";

    /**
     * 操作员
     */
    @XmlElement(name = "Operator")
    private String operator = ConfCenter.get("pay.cmb.operator");

    /**
     * 续传
     */
    @XmlElement(name = "pos")
    private String pos;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
