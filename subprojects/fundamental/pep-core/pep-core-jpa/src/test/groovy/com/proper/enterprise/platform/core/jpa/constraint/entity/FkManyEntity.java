package com.proper.enterprise.platform.core.jpa.constraint.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "PEP_FK_MANY_A")
public class FkManyEntity extends BaseEntity {

    private String name;

    @ManyToMany(mappedBy = "fkManyEntities")
    private Collection<UniqueEntity> uniqueEntities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<UniqueEntity> getUniqueEntities() {
        return uniqueEntities;
    }

    public void setUniqueEntities(Collection<UniqueEntity> uniqueEntities) {
        this.uniqueEntities = uniqueEntities;
    }
}
