package com.proper.enterprise.platform.auth.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "PEP_AUTH_USERS")
@CacheEntity
public class UserEntity extends BaseEntity implements User {

    public UserEntity() { }

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 用户名，唯一
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 邮箱，用于找回密码
     */
    private String email;

    /**
     * 是否为系统管理员
     */
    private boolean admin;

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_USERS_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Collection<RoleEntity> roleEntities = Collections.emptySet();

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + "]";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    @JsonIgnore
    public Collection<? extends Role> getRoles() {
        return roleEntities;
    }

    @Override
    public void addRole(Role role) {
        roleEntities.add((RoleEntity) role);
    }

    @Override
    public void removeRole(Role role) {
        roleEntities.remove(role);
    }

    public Collection<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    @Override
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
