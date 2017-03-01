package com.proper.enterprise.platform.pay.cmb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 单笔订单查询接口请求对象
 */
@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmbQuerySingleOrderReq implements Serializable {

    /**
     * 请求head
     */
    @XmlElement(name = "Head")
    private CmbQuerySingleOrderHeadReq head;

    /**
     * 请求body
     */
    @XmlElement(name = "Body")
    private CmbQuerySingleOrderBodyReq body;

    /**
     * Hash
     */
    @XmlElement(name = "Hash")
    private String hash;

    public CmbQuerySingleOrderHeadReq getHead() {
        return head;
    }

    public void setHead(CmbQuerySingleOrderHeadReq head) {
        this.head = head;
    }

    public CmbQuerySingleOrderBodyReq getBody() {
        return body;
    }

    public void setBody(CmbQuerySingleOrderBodyReq body) {
        this.body = body;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
