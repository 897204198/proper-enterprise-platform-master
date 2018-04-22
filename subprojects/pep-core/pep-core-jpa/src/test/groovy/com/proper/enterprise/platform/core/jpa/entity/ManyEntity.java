package com.proper.enterprise.platform.core.jpa.entity;

import com.proper.enterprise.platform.core.entity.BaseEntity;
import javax.persistence.*;

@Entity
@Table(name = "ManyEntity")
public class ManyEntity extends BaseEntity {

    @Column
    private Integer test;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oneEntity.id")
    private OneEntity oneEntity;

    public Integer getTest() {
        return test;
    }

    public ManyEntity setTest(Integer test) {
        this.test = test;
        return this;
    }

    public OneEntity getOneEntity() {
        return oneEntity;
    }

    public void setOneEntity(OneEntity oneEntity) {
        this.oneEntity = oneEntity;
    }
}
