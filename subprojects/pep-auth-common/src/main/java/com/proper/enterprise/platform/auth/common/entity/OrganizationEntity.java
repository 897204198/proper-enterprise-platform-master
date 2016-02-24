package com.proper.enterprise.platform.auth.common.entity;

import com.proper.enterprise.platform.api.auth.model.Organization;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "pep_auth_organization")
@CacheEntity
public class OrganizationEntity extends BaseEntity implements Organization {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationEntity.class);

    public OrganizationEntity() { }

    public OrganizationEntity(String name) {
        this.name = name;
    }

    /**
     * 机构编码
     */
    private String code;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 上级机构
     */
    @OneToOne
    private OrganizationEntity parentEntity;

    @OneToMany(mappedBy = "belongOrgEntity")
    private Collection<PositionEntity> positionEntities;

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
    public Organization getParent() {
        return parentEntity;
    }

    @Override
    public void setParent(Organization parent) {
        if (parent instanceof OrganizationEntity) {
            this.parentEntity = (OrganizationEntity)parent;
        } else {
            LOGGER.error("Error class type: {}", parent.getClass().getCanonicalName());
        }
    }

}
