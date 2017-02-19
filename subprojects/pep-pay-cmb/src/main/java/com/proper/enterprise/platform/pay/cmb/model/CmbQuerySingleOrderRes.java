package com.proper.enterprise.platform.pay.cmb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 单笔订单查询接口响应对象
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmbQuerySingleOrderRes {

    /**
     * 响应head
     */
    @XmlElement(name = "Head")
    private CmbCommonHeadRes head;

    /**
     * 响应body
     */
    @XmlElement(name = "Body")
    private CmbQuerySingleOrderBodyRes body;

    public CmbCommonHeadRes getHead() {
        return head;
    }

    public void setHead(CmbCommonHeadRes head) {
        this.head = head;
    }

    public CmbQuerySingleOrderBodyRes getBody() {
        return body;
    }

    public void setBody(CmbQuerySingleOrderBodyRes body) {
        this.body = body;
    }
}
