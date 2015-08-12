package com.proper.enterprise.platform.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.proper.enterprise.platform.core.entity.BaseEntity;

@Entity
@Table(name = "pep_auth_role_resource")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "com.proper.enterprise.platform.auth.entity.RoleResourceEntity")
public class RoleResourceEntity extends BaseEntity {

    private static final long serialVersionUID = -4804048854364046699L;
    
    public RoleResourceEntity(String roleId, String resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }
    
    /**
     * 角色id
     */
    @Column(nullable = false)
    private String roleId;
    
    /**
     * 资源id
     */
    @Column(nullable = false)
    private String resourceId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

}
