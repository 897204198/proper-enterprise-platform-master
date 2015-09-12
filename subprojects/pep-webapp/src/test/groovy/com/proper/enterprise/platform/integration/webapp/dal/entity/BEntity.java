package com.proper.enterprise.platform.integration.webapp.dal.entity;

import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pep_test_b")
@CacheEntity
public class BEntity extends BaseEntity {

    private static final long serialVersionUID = -6457329242417470550L;

    public BEntity(String name) {
        this.name = name;
    }

    @Column(nullable = false, unique = true)
    private String name;

    private String attr1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }
}
