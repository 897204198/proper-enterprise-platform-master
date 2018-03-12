package com.proper.enterprise.platform.auth.neo4j.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.neo4j.entity.BaseNodeEntity;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

@NodeEntity(label = "PEP_AUTH_USERGROUPS")
public class UserGroupNodeEntity extends BaseNodeEntity implements UserGroup {

    private String name;
    private String description;
    private int seq;
    private boolean enable = true;
    @Relationship(type = "has_user")
    private Set<UserNodeEntity> users = new HashSet<>();
    @Relationship(type = "has_role")
    private Set<RoleNodeEntity> roles = new HashSet<>();

    public UserGroupNodeEntity() {
    }

    public UserGroupNodeEntity(String name) {
        this.name = name;
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
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public void add(User user) {
        this.getUsers().add((UserNodeEntity) user);
    }

    @Override
    public void add(User... users) {
        Collection<User> userCollection = new ArrayList<>(users.length);
        Collections.addAll(userCollection, users);
        this.getUsers().addAll(CollectionUtil.convert(userCollection));
    }

    @Override
    public void add(Role role) {
        this.getRoles().add((RoleNodeEntity) role);
    }

    @Override
    public void remove(User user) {
        this.getUsers().remove(user);
    }

    @Override
    public void remove(Role role) {
        this.getRoles().remove(role);
    }

    @JsonIgnore
    @Override
    public Collection<UserNodeEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserNodeEntity> users) {
        this.users = users;
    }

    @JsonIgnore
    @Override
    public Collection<RoleNodeEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleNodeEntity> roles) {
        this.roles = roles;
    }
}
