package com.proper.enterprise.platform.core.jpa.curd.c.entity;

import com.proper.enterprise.platform.core.jpa.curd.a.entity.AEntity;
import com.proper.enterprise.platform.core.jpa.curd.b.entity.BEntity;
import com.proper.enterprise.platform.core.jpa.curd.c.api.C;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PEP_POJO_C")
public class CEntity extends BaseEntity implements C {

    public CEntity() {
        super.setEnable(true);
    }

    @Column
    private Integer test;

    @OneToMany(mappedBy = "centity")
    List<AEntity> aentitys;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "B_ID")
    private BEntity bentity;

    public Integer getTest() {
        return test;
    }

    public CEntity setTest(Integer test) {
        this.test = test;
        return this;
    }

    public List<AEntity> getAentitys() {
        return aentitys;
    }

    public CEntity setAentitys(List<AEntity> aentitys) {
        this.aentitys = aentitys;
        return this;
    }

    public BEntity getBentity() {
        return bentity;
    }

    public CEntity setBentity(BEntity bentity) {
        this.bentity = bentity;
        return this;
    }
}
