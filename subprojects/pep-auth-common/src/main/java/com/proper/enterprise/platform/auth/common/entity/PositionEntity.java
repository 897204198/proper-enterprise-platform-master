package com.proper.enterprise.platform.auth.common.entity;

import com.proper.enterprise.platform.api.auth.model.Organization;
import com.proper.enterprise.platform.api.auth.model.Position;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "PEP_AUTH_POSITIONS")
@CacheEntity
public class PositionEntity extends BaseEntity implements Position {

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionEntity.class);

    public PositionEntity(String name) {
        this.name = name;
    }

    /**
     * 所属机构
     */
    @ManyToOne
    @JoinColumn(name = "BELONG_ORG_ID")
    private OrganizationEntity belongOrgEntity;

    /**
     * 职位编码
     */
    private String code;

    /**
     * 职位名称
     */
    private String name;

    /**
     * 上级职位
     */
    @OneToOne
    @JoinColumn(name = "PARENT_POSITION_ID")
    private PositionEntity parentEntity;

    @ManyToMany(mappedBy = "positionEntities")
    private Collection<PersonEntity> personEntities;

    @Override
    public Organization getBelongOrg() {
        return belongOrgEntity;
    }

    @Override
    public void setBelongOrg(Organization belongOrg) {
        if (belongOrg instanceof OrganizationEntity) {
            this.belongOrgEntity = (OrganizationEntity)belongOrg;
        } else {
            LOGGER.error("Error class type: {}", belongOrg.getClass().getCanonicalName());
        }
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Position getParent() {
        return parentEntity;
    }

    @Override
    public void setParent(Position parent) {
        if (parent instanceof PositionEntity) {
            this.parentEntity = (PositionEntity)parent;
        } else {
            LOGGER.error("Error class type: {}", parent.getClass().getCanonicalName());
        }
    }

    public Collection<PersonEntity> getPersonEntities() {
        return personEntities;
    }

    public void setPersonEntities(Collection<PersonEntity> personEntities) {
        this.personEntities = personEntities;
    }
}
