package com.proper.enterprise.platform.auth.common.entity;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.api.auth.enums.UseStatus;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "PEP_AUTH_ROLES")
@CacheEntity
public class RoleEntity extends BaseEntity implements Role {

    private static final long serialVersionUID = PEPConstants.VERSION;

    public RoleEntity() { }

    public RoleEntity(String code) {
        this.code = code;
    }

    /**
     * code
     */
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * 名称
     */
    private String name;

    @ManyToMany(mappedBy = "roleEntities")
    private Collection<UserEntity> userEntities;

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_ROLES_RESOURCES",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "RESOURCE_ID"))
    private Collection<ResourceEntity> resourceEntities;

    /**
     * 使用状态
     */
    @Enumerated(EnumType.STRING)
    private UseStatus useStatus = UseStatus.STOP;

    @Override
    public String toString() {
        return "code: " + code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UseStatus getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(UseStatus useStatus) {
        this.useStatus = useStatus;
    }

    public Collection<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(Collection<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public Collection<ResourceEntity> getResourceEntities() {
        return resourceEntities;
    }

    public void setResourceEntities(Collection<ResourceEntity> resourceEntities) {
        this.resourceEntities = resourceEntities;
    }
}
