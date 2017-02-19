package com.proper.enterprise.platform.pay.cmb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 退款接口接口响应对象
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmbRefundNoDupRes {

    /**
     * 响应head
     */
    @XmlElement(name = "Head")
    private CmbCommonHeadRes head;

    /**
     * 响应body
     */
    @XmlElement(name = "Body")
    private CmbRefundNoDupBodyRes body;

    public CmbCommonHeadRes getHead() {
        return head;
    }

    public void setHead(CmbCommonHeadRes head) {
        this.head = head;
    }

    public CmbRefundNoDupBodyRes getBody() {
        return body;
    }

    public void setBody(CmbRefundNoDupBodyRes body) {
        this.body = body;
    }
}
