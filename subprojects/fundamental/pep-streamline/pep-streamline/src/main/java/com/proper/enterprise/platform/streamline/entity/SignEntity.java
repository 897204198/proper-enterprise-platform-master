package com.proper.enterprise.platform.streamline.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.streamline.model.Sign;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_STREAMLINE_SIGN")
public class SignEntity extends BaseEntity implements Sign {

    public SignEntity() {
    }

    /**
     * 业务Id
     */
    @Column(nullable = false)
    private String businessId;

    /**
     * 签名
     */
    @Column(nullable = false)
    private String signature;

    /**
     * 服务端标识
     */
    @Column(nullable = false)
    private String serviceKey;

    @Override
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public String getSignature() {
        return signature;
    }

    @Override
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String getServiceKey() {
        return serviceKey;
    }

    @Override
    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

}
