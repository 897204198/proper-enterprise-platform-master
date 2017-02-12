package com.proper.enterprise.platform.pay.cmb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 按订单号查询退款订单接口响应对象
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmbQueryRefundRes {

    /**
     * 响应head
     */
    @XmlElement(name = "Head")
    private CmbCommonHeadRes head;

    /**
     * 响应body
     */
    @XmlElement(name = "Body")
    private CmbQueryRefundBodyRes body;

    public CmbCommonHeadRes getHead() {
        return head;
    }

    public void setHead(CmbCommonHeadRes head) {
        this.head = head;
    }

    public CmbQueryRefundBodyRes getBody() {
        return body;
    }

    public void setBody(CmbQueryRefundBodyRes body) {
        this.body = body;
    }
}
