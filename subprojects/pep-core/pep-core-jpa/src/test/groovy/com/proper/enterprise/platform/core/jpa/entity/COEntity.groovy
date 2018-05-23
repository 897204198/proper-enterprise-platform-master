package com.proper.enterprise.platform.core.jpa.entity

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity

import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "pep_test_c")
@CacheEntity
class COEntity extends BaseEntity {

    private static final long serialVersionUID = PEPConstants.VERSION;

    public COEntity(String name) {
        this.name = name
    }

    String name

    @ManyToMany(mappedBy = 'cEntities')
    Collection<AOEntity> aEntities

}
