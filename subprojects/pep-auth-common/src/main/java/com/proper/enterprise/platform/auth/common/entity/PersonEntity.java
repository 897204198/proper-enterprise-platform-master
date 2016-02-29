package com.proper.enterprise.platform.auth.common.entity;

import com.proper.enterprise.platform.api.auth.enums.GenderType;
import com.proper.enterprise.platform.api.auth.model.Person;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "pep_auth_persons")
@CacheEntity
public class PersonEntity extends BaseEntity implements Person {

    public PersonEntity() { }

    public PersonEntity(String name, String idCard, GenderType gender) {
        this.name = name;
        this.idCard = idCard;
        this.gender = gender;
    }

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    private GenderType gender = GenderType.MALE;

    /**
     * 身份证号
     */
    @Column(nullable = false, unique = true)
    private String idCard;

    /**
     * 姓名
     */
    private String name;

    @Override
    public String toString() {
        return "[id: " + id + ", name: " + name + ", gender: " + gender + "]";
    }

    @OneToMany(mappedBy = "personEntity")
    private Collection<UserEntity> userEntities;

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_PERSONS_POSITIONS",
            joinColumns = @JoinColumn(name = "PERSON_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "POSITION_ID", referencedColumnName = "ID"))
    private Collection<PositionEntity> positionEntities;

    @Override
    public GenderType getGender() {
        return gender;
    }

    @Override
    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    @Override
    public String getIdCard() {
        return idCard;
    }

    @Override
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Collection<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(Collection<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public Collection<PositionEntity> getPositionEntities() {
        return positionEntities;
    }

    public void setPositionEntities(Collection<PositionEntity> positionEntities) {
        this.positionEntities = positionEntities;
    }
}
