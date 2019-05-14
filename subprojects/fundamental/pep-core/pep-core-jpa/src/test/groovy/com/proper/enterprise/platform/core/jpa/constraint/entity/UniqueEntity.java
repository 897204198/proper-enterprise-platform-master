package com.proper.enterprise.platform.core.jpa.constraint.entity;

import com.proper.enterprise.platform.core.jpa.annotation.ConstraintViolationMessage;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "PEP_UNIQUE_A")
public class UniqueEntity extends BaseEntity {

    @Column(unique = true)
    @ConstraintViolationMessage(name = "UK_TD7CI9HAE6GOL91BPMVVOINVC_INDEX_2", message = "code cant be repeat")
    private String code;

    @OneToOne
    @JoinColumn(name = "fk_id", foreignKey = @ForeignKey(name = "TEST_FK_NAME"))
    @ConstraintViolationMessage(name = "TEST_FK_NAME", message = "cant delete fk because have unique used")
    private FkEntity fkEntity;

    @ManyToMany
    @JoinTable(name = "PEP_UNIQUE_FKMANY",
        joinColumns = @JoinColumn(name = "UNIQUE_ID"),
        inverseJoinColumns = @JoinColumn(name = "FK_MANY_ID"),
        inverseForeignKey = @ForeignKey(name = "TEST_FK_MANY_NAME"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"UNIQUE_ID", "FK_MANY_ID"}))
    @ConstraintViolationMessage(name = "TEST_FK_MANY_NAME", message = "cant delete fk many because have unique used")
    private Collection<FkManyEntity> fkManyEntities;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public FkEntity getFkEntity() {
        return fkEntity;
    }

    public void setFkEntity(FkEntity fkEntity) {
        this.fkEntity = fkEntity;
    }

    public Collection<FkManyEntity> getFkManyEntities() {
        return fkManyEntities;
    }

    public void setFkManyEntities(Collection<FkManyEntity> fkManyEntities) {
        this.fkManyEntities = fkManyEntities;
    }
}
