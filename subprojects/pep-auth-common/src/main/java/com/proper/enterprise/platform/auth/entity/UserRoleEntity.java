package com.proper.enterprise.platform.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.proper.enterprise.platform.core.entity.BaseEntity;

@Entity
@Table(name = "pep_auth_user_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "com.proper.enterprise.platform.auth.entity.UserRoleEntity")
public class UserRoleEntity extends BaseEntity {

    private static final long serialVersionUID = 8763871766032994582L;
    
    public UserRoleEntity(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
    
    /**
     * 角色id
     */
    @Column(nullable = false)
    private String roleId;
    
    /**
     * 用户id
     */
    @Column(nullable = false)
    private String userId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
