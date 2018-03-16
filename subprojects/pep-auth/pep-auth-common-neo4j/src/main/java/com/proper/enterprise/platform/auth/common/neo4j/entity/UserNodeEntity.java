package com.proper.enterprise.platform.auth.common.neo4j.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.neo4j.entity.BaseNodeEntity;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

@NodeEntity(label = "PEP_AUTH_USERS")
public class UserNodeEntity extends BaseNodeEntity implements User {

    private String username;
    private String password;
    private String email;
    private boolean superuser;
    private String phone;
    private String name = " ";
    private boolean enable = true;
    protected String pepDtype;

    @Relationship(type = "has_role")
    private Set<RoleNodeEntity> roleNodes = new HashSet<>();

    @Relationship(type = "has_user", direction = Relationship.INCOMING)
    private Set<UserGroupNodeEntity> gourps = new HashSet<>();

    public UserNodeEntity() {
    }

    public UserNodeEntity(String username, String password) {
        this.username = username;
        this.password = password;
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
    public boolean isSuperuser() {
        return superuser;
    }

    @JsonIgnore
    @Override
    public Collection<UserGroupNodeEntity> getUserGroups() {
        return this.getGourps();
    }

    @JsonIgnore
    public Set<UserGroupNodeEntity> getGourps() {
        return gourps;
    }

    public void setGourps(Set<UserGroupNodeEntity> gourps) {
        this.gourps = gourps;
    }

    public void add(UserGroupNodeEntity userGroupNodeEntity) {
        this.getGourps().add(userGroupNodeEntity);
    }

    @Override
    public void add(Role role) {
        this.getRoleNodes().add((RoleNodeEntity) role);
    }

    public void remove(UserGroupNodeEntity userGroupNodeEntity) {
        this.getGourps().remove(userGroupNodeEntity);
    }

    @Override
    public void remove(Role role) {
        this.getRoleNodes().remove((RoleNodeEntity) role);
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
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

    @JsonIgnore
    @Override
    public Collection<RoleNodeEntity> getRoles() {
        return roleNodes;
    }

    @JsonIgnore
    public Set<RoleNodeEntity> getRoleNodes() {
        return roleNodes;
    }

    public void setRoleNodes(Set<RoleNodeEntity> roleNodes) {
        this.roleNodes = roleNodes;
    }

    public void addRole(RoleNodeEntity roleNodeEntity) {
        this.getRoleNodes().add(roleNodeEntity);
    }

    public String getPepDtype() {
        return pepDtype;
    }

    public void setPepDtype(String pepDtype) {
        this.pepDtype = pepDtype;
    }

}
