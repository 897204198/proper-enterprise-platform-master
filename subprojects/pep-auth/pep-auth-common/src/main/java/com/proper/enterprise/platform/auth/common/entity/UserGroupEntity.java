package com.proper.enterprise.platform.auth.common.entity;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.hibernate.annotations.Type;

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
    @Column
    private String description;

    /**
     * 顺序
     */
    @Column
    private int seq;

    /**
     * 用户组内用户列信息列表
     */
    @Transient
    private Collection<? extends User> users = new ArrayList<>();

    /**
     * 用户组内角色列信息列表
     */
    @Transient
    private Collection<? extends Role> roles = new ArrayList<>();

    /**
     * 用户组状态
     */
    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private boolean enable = true;

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
    public void add(User user) {
        userEntities.add((UserEntity) user);
    }

    @Override
    public void add(User... users) {
        Collection<User> userCollection = new ArrayList<>(users.length);
        Collections.addAll(userCollection, users);
        userEntities.addAll(CollectionUtil.convert(userCollection));
    }

    @Override
    public void add(Role role) {
        roleGroupEntities.add((RoleEntity) role);
    }

    @Override
    public void remove(User user) {
        userEntities.remove(user);
    }

    @Override
    public void remove(Role role) {
        roleGroupEntities.remove(role);
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
    public Collection<? extends User> getUsers() {
        // TODO
        return userEntities;
    }

    @Override
    public Collection<? extends Role> getRoles() {
        // TODO
        return roleGroupEntities;
    }
}
