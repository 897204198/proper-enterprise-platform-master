package com.proper.enterprise.platform.pay.cmb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 已结账单 请求对象
 */
@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmbBillReq implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 请求head
     */
    @XmlElement(name = "Head")
    private CmbBillHeadReq head;

    /**
     * 请求body
     */
    @XmlElement(name = "Body")
    private CmbBillBodyReq body;

    /**
     * Hash
     */
    @XmlElement(name = "Hash")
    private String hash;

    public CmbBillHeadReq getHead() {
        return head;
    }

    public void setHead(CmbBillHeadReq head) {
        this.head = head;
    }

    public CmbBillBodyReq getBody() {
        return body;
    }

    public void setBody(CmbBillBodyReq body) {
        this.body = body;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
