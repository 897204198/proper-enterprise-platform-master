package com.proper.enterprise.platform.core.jpa.entity

import com.proper.enterprise.platform.core.PEPVersion
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "pep_test_b")
@CacheEntity
class BOEntity extends BaseEntity {

    private static final long serialVersionUID = PEPVersion.VERSION

    BOEntity(String name) {
        this.name = name
    }

    @Column(nullable = false, unique = true)
    String name

    String attr1

}
