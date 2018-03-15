package com.proper.enterprise.platform.configs.dal.entity

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.entity.BaseEntity
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity

import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "pep_test_c")
@CacheEntity
class CEntity extends BaseEntity {

    private static final long serialVersionUID = PEPConstants.VERSION;

    public CEntity(String name) {
        this.name = name
    }

    String name

    @ManyToMany(mappedBy = 'cEntities')
    Collection<AEntity> aEntities

}
