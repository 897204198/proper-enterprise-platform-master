package com.proper.enterprise.platform.integration.webapp.dal.entity

import com.proper.enterprise.platform.core.annotation.CacheEntity
import com.proper.enterprise.platform.core.entity.BaseEntity

import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "pep_test_c")
@CacheEntity
class CEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public CEntity(String name) {
        this.name = name
    }

    String name

    @ManyToMany(mappedBy = 'cEntities')
    Collection<AEntity> aEntities

}
