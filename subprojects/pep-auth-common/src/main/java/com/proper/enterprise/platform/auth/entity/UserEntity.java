package com.proper.enterprise.platform.auth.entity;

import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "pep_auth_user")
@Cacheable
public class UserEntity extends BaseEntity {

    private static final long serialVersionUID = -932921035920514049L;

    public UserEntity() { }
    
    public UserEntity(String loginName, String password) {
        this.loginName = loginName;
        this.password = password;
    }
    
    /**
     * 登录名，唯一
     */
    @Column(unique = true, nullable = false)
    private String loginName;
    
    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮箱，用于找回密码
     */
    private String email;

    /**
     * override property in super class
     */
    private String extendProperties = "";

    @ManyToMany
    private Collection<RoleEntity> roles;
    
    @Override
    public String toString() {
        return "User [id=" + id + ", loginName=" + loginName + ", name=" + name
                + ", extendProperties=" + extendProperties + "]";
    }
    
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleEntity> roles) {
        this.roles = roles;
    }

}
