package com.proper.enterprise.platform.core.jpa.copy.entity;

import com.proper.enterprise.platform.core.jpa.copy.api.CopyA;
import com.proper.enterprise.platform.core.jpa.copy.api.CopyB;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class CopyAEntity extends BaseEntity implements CopyA {

    private String name;

    @OneToOne
    @JoinColumn(name = "B_ID")
    private CopyBEntity cb;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CopyBEntity getCb() {
        return cb;
    }

    @Override
    public void setCb(CopyB b) {
        this.cb = (CopyBEntity) b;
    }
}
