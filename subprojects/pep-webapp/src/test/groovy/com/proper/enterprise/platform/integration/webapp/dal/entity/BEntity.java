package com.proper.enterprise.platform.integration.webapp.dal.entity;

import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "pep_test_b")
@Cacheable
public class BEntity extends BaseEntity {

    public BEntity(String name) {
        this.name = name;
    }

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "bs")
    private Collection<AEntity> as;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<AEntity> getAs() {
        return as;
    }

    public void setAs(Collection<AEntity> as) {
        this.as = as;
    }

}
