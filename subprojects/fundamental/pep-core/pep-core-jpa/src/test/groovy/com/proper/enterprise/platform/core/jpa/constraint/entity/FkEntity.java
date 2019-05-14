package com.proper.enterprise.platform.core.jpa.constraint.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_FK_A")
public class FkEntity extends BaseEntity {

    private String name;

    @OneToOne(mappedBy = "fkEntity")
    private UniqueEntity uniqueEntity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UniqueEntity getUniqueEntity() {
        return uniqueEntity;
    }

    public void setUniqueEntity(UniqueEntity uniqueEntity) {
        this.uniqueEntity = uniqueEntity;
    }
}
