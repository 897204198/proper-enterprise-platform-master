package com.proper.enterprise.platform.core.jpa.curd.b.entity;

import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.c.entity.CEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.jpa.curd.a.entity.AEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PEP_POJO_B")
public class BEntity extends BaseEntity implements B {

    public BEntity() {
        super.setEnable(true);
    }

    @Column
    private Integer test;

    @ManyToMany(mappedBy = "bentities", fetch = FetchType.LAZY)
    private List<AEntity> aentities;

    @OneToOne(mappedBy = "bentity", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private CEntity centity;

    @Column
    private String doStrB;


    public Integer getTest() {
        return test;
    }

    public BEntity setTest(Integer test) {
        this.test = test;
        return this;
    }

    public List<AEntity> getAentities() {
        return aentities;
    }

    public BEntity setAentities(List<AEntity> aentities) {
        this.aentities = aentities;
        return this;
    }

    public CEntity getCentity() {
        return centity;
    }

    public BEntity setCentity(CEntity centity) {
        this.centity = centity;
        return this;
    }

    public String getDoStrB() {
        return doStrB;
    }

    public BEntity setDoStrB(String doStrB) {
        this.doStrB = doStrB;
        return this;
    }


}
