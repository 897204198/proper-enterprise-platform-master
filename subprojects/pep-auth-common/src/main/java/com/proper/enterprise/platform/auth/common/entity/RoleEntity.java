package com.proper.enterprise.platform.auth.common.entity;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.api.auth.enums.UseStatus;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "pep_auth_roles")
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

    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;

    @ManyToMany
    private Collection<ResourceEntity> resources;

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

    public Collection<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserEntity> users) {
        this.users = users;
    }

    public Collection<ResourceEntity> getResources() {
        return resources;
    }

    public void setResources(Collection<ResourceEntity> resources) {
        this.resources = resources;
    }

}
