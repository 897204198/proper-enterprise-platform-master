package com.proper.enterprise.platform.auth.common.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
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
        this.superuser = false;
        super.setEnable(true);
    }

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
        this.superuser = false;
        super.setEnable(true);
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
    private Boolean superuser;

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

    @Column(insertable = false, updatable = false)
    protected String pepDtype;

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_USERS_ROLES",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"USER_ID", "ROLE_ID"}))
    private Collection<RoleEntity> roleEntities;

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
    private Collection<UserGroupEntity> userGroupEntities;

    @Column
    private String avatar;

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + "]";
    }


    public String getPepDtype() {
        return pepDtype;
    }

    public void setPepDtype(String pepDtype) {
        this.pepDtype = pepDtype;
    }

    @Override
    public Boolean getSuperuser() {
        return superuser;
    }

    public void setSuperuser(Boolean superuser) {
        this.superuser = superuser;
    }

    public Collection<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(Collection<RoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }

    public Collection<UserGroupEntity> getUserGroupEntities() {
        return userGroupEntities;
    }

    public void setUserGroupEntities(Collection<UserGroupEntity> userGroupEntities) {
        this.userGroupEntities = userGroupEntities;
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
    public void add(Role role) {
        if (null == roleEntities) {
            roleEntities = new ArrayList<>();
        }
        roleEntities.add((RoleEntity) role);
    }

    @Override
    public void add(UserGroup userGroup) {
        if (null == userGroupEntities) {
            userGroupEntities = new ArrayList<>();
        }
        userGroupEntities.add((UserGroupEntity) userGroup);
    }

    @Override
    public void remove(Role role) {
        if (null == roleEntities) {
            return;
        }
        roleEntities.remove(role);
    }


    @Override
    public void remove(UserGroup userGroup) {
        if (null == userGroupEntities) {
            return;
        }
        userGroupEntities.remove(userGroup);
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
