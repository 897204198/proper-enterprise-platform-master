package com.proper.enterprise.platform.auth.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
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
        return obj != null && (obj instanceof UserEntity) && id.equals(((UserEntity) obj).id);
    }

    @ManyToMany(mappedBy = "userEntities")
    private Collection<UserGroupEntity> userGroupEntities = new ArrayList<>();

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    @JsonIgnore
    public Collection<? extends Role> getRoles() {
        return roleEntities;
    }

    @Override
    public void add(Role role) {
        roleEntities.add((RoleEntity) role);
    }

    @Override
    public void remove(Role role) {
        roleEntities.remove(role);
    }

    public Collection<RoleEntity> getRoleEntities() {
        return roleEntities;
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
    public Collection<? extends UserGroup> getUserGroups() {
        return userGroupEntities;
    }

}
