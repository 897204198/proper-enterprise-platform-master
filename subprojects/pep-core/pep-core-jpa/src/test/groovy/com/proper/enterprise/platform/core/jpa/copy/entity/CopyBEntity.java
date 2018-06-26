package com.proper.enterprise.platform.core.jpa.copy.entity;

import com.proper.enterprise.platform.core.jpa.copy.api.CopyA;
import com.proper.enterprise.platform.core.jpa.copy.api.CopyB;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class CopyBEntity extends BaseEntity implements CopyB {

    private String bname;

    @OneToOne(mappedBy = "cb")
    private CopyAEntity ca;

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public CopyAEntity getCa() {
        return ca;
    }

    public void setCa(CopyA a) {
        this.ca = (CopyAEntity) a;
    }
}
