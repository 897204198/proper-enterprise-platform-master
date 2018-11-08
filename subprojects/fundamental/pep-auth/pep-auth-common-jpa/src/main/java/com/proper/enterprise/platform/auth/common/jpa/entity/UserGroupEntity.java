package com.proper.enterprise.platform.auth.common.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.CollectionUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
@CacheEntity
@Table(name = "PEP_AUTH_USERGROUPS")
public class UserGroupEntity extends BaseEntity implements UserGroup {

    public UserGroupEntity() {
    }

    public UserGroupEntity(String name) {
        this.name = name;
    }

    /**
     * 用户组名称
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 描述
     */
    @Column
    private String description;

    /**
     * 顺序
     */
    @Column
    private int seq;

    @ManyToMany(mappedBy = "userGroupEntities")
    private Collection<UserEntity> userEntities;

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_GROUPS_ROLES",
        joinColumns = @JoinColumn(name = "GROUP_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"GROUP_ID", "ROLE_ID"}))
    private Collection<RoleEntity> roleGroupEntities;


    public Collection<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(Collection<UserEntity> userEntities) {
        this.userEntities = userEntities;
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
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getSeq() {
        return seq;
    }

    @Override
    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public void add(User user) {
        if (null == userEntities) {
            userEntities = new ArrayList<>();
        }
        userEntities.add((UserEntity) user);
    }

    @Override
    public void add(User... users) {
        if (null == userEntities) {
            userEntities = new ArrayList<>();
        }
        Collection<User> userCollection = new ArrayList<>(users.length);
        Collections.addAll(userCollection, users);
        userEntities.addAll(CollectionUtil.convert(userCollection));
    }

    @Override
    public void add(Role role) {
        if (null == roleGroupEntities) {
            roleGroupEntities = new ArrayList<>();
        }
        roleGroupEntities.add((RoleEntity) role);
    }

    @Override
    public void remove(User user) {
        if (null == userEntities) {
            userEntities = new ArrayList<>();
        }
        userEntities.remove(user);
    }

    @Override
    public void remove(Role role) {
        if (null == roleGroupEntities) {
            roleGroupEntities = new ArrayList<>();
        }
        roleGroupEntities.remove(role);
    }

    @Override
    @JsonIgnore
    public Collection<? extends User> getUsers() {
        return userEntities;
    }

    @Override
    @JsonIgnore
    public Collection<? extends Role> getRoles() {
        return roleGroupEntities;
    }


    @Override
    public void removeAllUsers(Collection<? extends User> users) {
        if (null == userEntities) {
            userEntities = new ArrayList<>();
        }
        userEntities.removeAll(CollectionUtil.convert(users));
    }


    public Collection<RoleEntity> getRoleGroupEntities() {
        return roleGroupEntities;
    }

    public void setRoleGroupEntities(Collection<RoleEntity> roleGroupEntities) {
        this.roleGroupEntities = roleGroupEntities;
    }
}
