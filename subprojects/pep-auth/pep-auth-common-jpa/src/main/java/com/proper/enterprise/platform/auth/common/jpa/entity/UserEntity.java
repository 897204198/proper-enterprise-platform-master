package com.proper.enterprise.platform.auth.common.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "PEP_AUTH_USERS")
@DiscriminatorColumn(name = "pepDtype")
@DiscriminatorValue("UserEntity")
@CacheEntity
public class UserEntity extends BaseEntity implements User {

    public UserEntity() {
    }

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
    @Column
    private String email;

    /**
     * 是否为超级用户
     */
    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private boolean superuser;

    /**
     * 用户手机号
     */
    @Column
    private String phone;

    /**
     * 用户显示名称
     */
    @Column
    private String name = " ";

    /**
     * 用户状态
     */
    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private boolean enable = true;

    @Column(insertable = false, updatable = false)
    protected String pepDtype;

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_USERS_ROLES",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"USER_ID", "ROLE_ID"}))
    private Collection<RoleEntity> roleEntities = new ArrayList<>();

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof UserEntity) && id.equals(((UserEntity) obj).id);
    }

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_USERS_GROUPS",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "USER_GROUP_ID"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"USER_ID", "USER_GROUP_ID"}))
    private Collection<UserGroupEntity> userGroupEntities = new ArrayList<>();

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + "]";
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
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
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public void add(Role role) {
        roleEntities.add((RoleEntity) role);
    }

    @Override
    public void add(UserGroup userGroup) {
        userGroupEntities.add((UserGroupEntity) userGroup);
    }

    @Override
    public void remove(Role role) {
        roleEntities.remove(role);
    }


    @Override
    public void remove(UserGroup userGroup) {
        userGroupEntities.remove(userGroup);
    }

    @Override
    public boolean isSuperuser() {
        return superuser;
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
    }

    public String getPepDtype() {
        return pepDtype;
    }

    public void setPepDtype(String pepDtype) {
        this.pepDtype = pepDtype;
    }

    @Override
    @JsonIgnore
    public Collection<? extends UserGroup> getUserGroups() {
        return userGroupEntities;
    }

    @Override
    @JsonIgnore
    public Collection<? extends Role> getRoles() {
        return roleEntities;
    }
}
