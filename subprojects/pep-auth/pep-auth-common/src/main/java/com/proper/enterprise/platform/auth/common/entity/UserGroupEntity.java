package com.proper.enterprise.platform.auth.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.CollectionUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
@CacheEntity
@Table(name = "PEP_AUTH_USERGROUPS")
public class UserGroupEntity extends BaseEntity implements UserGroup {

    public UserGroupEntity() { }

    public UserGroupEntity(String name) {
        this.name = name;
    }

    /**
     * 用户组名称
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 顺序
     */
    private int seq;

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_GROUPS_USERS",
            joinColumns = @JoinColumn(name = "USER_GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"USER_GROUP_ID", "USER_ID"}))
    private Collection<UserEntity> userEntities = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_GROUPS_ROLES",
        joinColumns = @JoinColumn(name = "GROUP_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"GROUP_ID", "ROLE_ID"}))
    private Collection<RoleEntity> roleGroupEntities = new ArrayList<>();

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
    @JsonIgnore
    public Collection<? extends User> getUsers() {
        return userEntities;
    }

    @Override
    public void add(User user) {
        userEntities.add((UserEntity) user);
    }

    @Override
    public void add(User... users) {
        Collection<User> userCollection = new ArrayList<>(users.length);
        Collections.addAll(userCollection, users);
        userEntities.addAll(CollectionUtil.convert(userCollection));
    }

    public Collection<UserEntity> getUserEntities() {
        return userEntities;
    }

    @Override
    public void remove(User user) {
        userEntities.remove(user);
    }
}
