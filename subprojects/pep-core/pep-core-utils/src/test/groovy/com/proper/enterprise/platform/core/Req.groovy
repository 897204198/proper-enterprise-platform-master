package com.proper.enterprise.platform.core

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "REQ")
public class Req {

    @XmlElement(name = "HOS_ID")
    private String hosId;

    @XmlElement(name = "IP")
    private String ip;

    public String getHosId() {
        return hosId
    }

    public void setHosId(String hosId) {
        this.hosId = hosId
    }

    public String getIp() {
        return ip
    }

    public void setIp(String ip) {
        this.ip = ip
    }

}
