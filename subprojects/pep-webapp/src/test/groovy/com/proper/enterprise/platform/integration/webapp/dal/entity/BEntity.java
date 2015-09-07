package com.proper.enterprise.platform.integration.webapp.dal.entity;

import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pep_test_b")
@Cacheable
public class BEntity extends BaseEntity {

    public BEntity(String name) {
        this.name = name;
    }

    @Column(nullable = false, unique = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
