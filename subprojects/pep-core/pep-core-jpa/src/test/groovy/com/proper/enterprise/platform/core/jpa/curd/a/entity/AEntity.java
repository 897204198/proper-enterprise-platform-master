package com.proper.enterprise.platform.core.jpa.curd.a.entity;

import com.proper.enterprise.platform.core.jpa.curd.a.api.A;
import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.b.entity.BEntity;
import com.proper.enterprise.platform.core.jpa.curd.c.api.C;
import com.proper.enterprise.platform.core.jpa.curd.c.entity.CEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PEP_POJO_A")
public class AEntity extends BaseEntity implements A {

    public AEntity() {
        super.setEnable(true);
    }

    @Column
    private Integer test;

    @Column
    private String doStr;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PEP_POJO_A_B",
        joinColumns = @JoinColumn(name = "A_ID"),
        inverseJoinColumns = @JoinColumn(name = "B_ID"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"A_ID", "B_ID"}))
    private List<BEntity> bentities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centity.id")
    private CEntity centity;

    public Integer getTest() {
        return test;
    }

    public AEntity setTest(Integer test) {
        this.test = test;
        return this;
    }

    public String getDoStr() {
        return doStr;
    }

    public void setDoStr(String doStr) {
        this.doStr = doStr;
    }

    public List<BEntity> getBentities() {
        return bentities;
    }

    public AEntity setBentities(List<BEntity> bentities) {
        this.bentities = bentities;
        return this;
    }

    @Override
    public AEntity add(B b) {
        if (null == bentities) {
            bentities = new ArrayList<>();
        }
        bentities.add((BEntity) b);
        return this;
    }


    @Override
    public List<? extends B> getBs() {
        return bentities;
    }

    @Override
    public AEntity setBs(List<? extends B> bs) {
        this.bentities = (List<BEntity>) bs;
        return this;
    }

    @Override
    public CEntity getCentity() {
        return centity;
    }

    @Override
    public AEntity setCentity(C centity) {
        this.centity = (CEntity) centity;
        return this;
    }
}
