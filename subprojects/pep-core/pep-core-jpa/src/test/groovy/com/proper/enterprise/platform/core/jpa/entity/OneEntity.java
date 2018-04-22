package com.proper.enterprise.platform.core.jpa.entity;

import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OneEntity")
public class OneEntity extends BaseEntity {

    @Column
    private Integer test;

    @OneToMany(mappedBy = "oneEntity")
    private List<ManyEntity> manyEntities;

    public Integer getTest() {
        return test;
    }

    public OneEntity setTest(Integer test) {
        this.test = test;
        return this;
    }

    public List<ManyEntity> getManyEntities() {
        return manyEntities;
    }

    public void setManyEntities(List<ManyEntity> manyEntities) {
        this.manyEntities = manyEntities;
    }

    public OneEntity add(ManyEntity manyEntity) {
        if (null == manyEntity) {
            return this;
        }
        if (null == this.manyEntities) {
            this.manyEntities = new ArrayList<>();
        }
        this.manyEntities.add(manyEntity);
        return this;
    }
}
