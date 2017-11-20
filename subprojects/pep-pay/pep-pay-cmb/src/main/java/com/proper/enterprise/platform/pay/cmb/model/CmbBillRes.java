package com.proper.enterprise.platform.pay.cmb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 已结账单 响应对象
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmbBillRes {

    /**
     * 响应head
     */
    @XmlElement(name = "Head")
    private CmbCommonHeadRes head;

    /**
     * 响应body
     */
    @XmlElement(name = "Body")
    private CmbBillBodyRes body;

    public CmbCommonHeadRes getHead() {
        return head;
    }

    public void setHead(CmbCommonHeadRes head) {
        this.head = head;
    }

    public CmbBillBodyRes getBody() {
        return body;
    }

    public void setBody(CmbBillBodyRes body) {
        this.body = body;
    }
}
