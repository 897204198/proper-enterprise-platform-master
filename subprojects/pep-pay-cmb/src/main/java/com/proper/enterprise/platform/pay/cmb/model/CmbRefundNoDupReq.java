package com.proper.enterprise.platform.pay.cmb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 退款接口请求对象
 */
@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmbRefundNoDupReq implements Serializable {

    /**
     * 请求head
     */
    @XmlElement(name = "Head")
    private CmbRefundNoDupHeadReq head;

    /**
     * 请求body
     */
    @XmlElement(name = "Body")
    private CmbRefundNoDupBodyReq body;

    /**
     * Hash
     */
    @XmlElement(name = "Hash")
    private String hash;

    public CmbRefundNoDupHeadReq getHead() {
        return head;
    }

    public void setHead(CmbRefundNoDupHeadReq head) {
        this.head = head;
    }

    public CmbRefundNoDupBodyReq getBody() {
        return body;
    }

    public void setBody(CmbRefundNoDupBodyReq body) {
        this.body = body;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
