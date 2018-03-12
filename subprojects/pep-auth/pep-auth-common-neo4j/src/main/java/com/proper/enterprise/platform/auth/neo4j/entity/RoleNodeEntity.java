package com.proper.enterprise.platform.auth.neo4j.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.core.neo4j.entity.BaseNodeEntity;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import javax.persistence.Transient;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = "PEP_AUTH_ROLES")
public class RoleNodeEntity extends BaseNodeEntity implements Role {

    private String name;
    private String description;
    @Transient
    private String parentId;
    private boolean enable = true;
    @Relationship(type = "parent_to")
    private RoleNodeEntity parent;

    @Relationship(type = "has_role", direction = Relationship.INCOMING)
    private Set<UserGroupNodeEntity> groups = new HashSet<>();

    @Relationship(type = "has_role", direction = Relationship.INCOMING)
    private Set<UserNodeEntity> userNodes = new HashSet<>();

    @Relationship(type = "has_menu")
    private Set<MenuNodeEntity> menus = new HashSet<>();

    @Relationship(type = "has_resource")
    private Set<ResourceNodeEntity> resources = new HashSet<>();

    public RoleNodeEntity() {
    }

    public RoleNodeEntity(String name) {
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
    public String getParentId() {
        return parent == null ? null : parent.getId();
    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
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
    public RoleNodeEntity getParent() {
        return parent;
    }

    @Override
    public void setParent(Role parent) {
        this.parent = (RoleNodeEntity) parent;
    }

    @JsonIgnore
    public Set<UserGroupNodeEntity> getGroups() {
        return groups;
    }

    public void setGroups(Set<UserGroupNodeEntity> groups) {
        this.groups = groups;
    }

    @JsonIgnore
    @Override
    public Collection<UserGroupNodeEntity> getUserGroups() {
        return groups;
    }

    public void add(UserGroupNodeEntity userGroupNodeEntity) {
        this.groups.add(userGroupNodeEntity);
    }

    public void add(MenuNodeEntity menuNodeEntity) {
        this.getMenus().add(menuNodeEntity);
    }

    @Override
    public void add(Collection<? extends Menu> menus) {
        this.getMenus().addAll(CollectionUtil.convert(menus));
    }

    public void addUserNode(UserNodeEntity userNodeEntity) {
        this.getUserNodes().add(userNodeEntity);
    }

    public void remove(UserGroupNodeEntity userGroupNodeEntity) {
        this.groups.remove(userGroupNodeEntity);
    }

    public void remove(MenuNodeEntity menuNodeEntity) {
        this.getMenus().remove(menuNodeEntity);
    }

    @Override
    public void remove(Collection<? extends Menu> menus) {
        this.getMenus().removeAll(CollectionUtil.convert(menus));
    }

    public void remove(UserNodeEntity userNodeEntity) {
        this.getUserNodes().remove(userNodeEntity);
    }

    @JsonIgnore
    @Override
    public Collection<MenuNodeEntity> getMenus() {
        return menus;
    }

    public void setMenus(Set<MenuNodeEntity> menus) {
        this.menus = menus;
    }

    @JsonIgnore
    @Override
    public Collection<ResourceNodeEntity> getResources() {
        return resources;
    }

    public void setResources(Set<ResourceNodeEntity> resources) {
        this.resources = resources;
    }

    public void addResource(ResourceNodeEntity resourceNodeEntity) {
        resources.add(resourceNodeEntity);
    }

    public void removeResource(ResourceNodeEntity resourceNodeEntity) {
        resources.remove(resourceNodeEntity);
    }

    @Override
    public void addResources(Collection<? extends Resource> resources) {
        this.getResources().addAll(CollectionUtil.convert(resources));
    }

    @Override
    public void removeResources(Collection<? extends Resource> resources) {
        this.getResources().removeAll(CollectionUtil.convert(resources));
    }

    @JsonIgnore
    @Override
    public Collection<UserNodeEntity> getUsers() {
        return userNodes;
    }

    @JsonIgnore
    public Set<UserNodeEntity> getUserNodes() {
        return userNodes;
    }

    public void setUserNodes(Set<UserNodeEntity> userNodes) {
        this.userNodes = userNodes;
    }

}
