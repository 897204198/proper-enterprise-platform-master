package com.proper.enterprise.platform.auth.common.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.CollectionUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "PEP_AUTH_ROLES")
@CacheEntity
public class RoleEntity extends BaseEntity implements Role {

    public RoleEntity() {
    }

    /**
     * 名称
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 权限描述
     */
    @Column
    private String description;

    /**
     * 父菜单
     */
    @ManyToOne
    @JoinColumn(name = "PARENT_ID", foreignKey = @ForeignKey(name = "null"))
    @JsonIgnore
    private RoleEntity parent;

    @Transient
    private String parentId;

    @ManyToMany(mappedBy = "roleEntities")
    private Collection<UserEntity> userEntities;

    @ManyToMany(mappedBy = "roleGroupEntities")
    private Collection<UserGroupEntity> userGroupEntities;

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_ROLES_MENUS",
        joinColumns = @JoinColumn(name = "ROLE_ID"),
        inverseJoinColumns = @JoinColumn(name = "MENU_ID"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"ROLE_ID", "MENU_ID"}))
    private Collection<MenuEntity> menuEntities;

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_ROLES_RESOURCES",
        joinColumns = @JoinColumn(name = "ROLE_ID"),
        inverseJoinColumns = @JoinColumn(name = "RESOURCE_ID"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"ROLE_ID", "RESOURCE_ID"}))
    private Collection<ResourceEntity> resourcesEntities;

    public String getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        return "name: " + name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (obj instanceof RoleEntity) && id.equals(((RoleEntity) obj).id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
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
    public Role getParent() {
        return parent;
    }

    @Override
    public void addParent(Role parent) {
        if (null == parent) {
            this.parent = null;
            return;
        }
        this.parent = (RoleEntity) parent;
    }

    @Override
    @JsonIgnore
    public Collection<? extends User> getUsers() {
        return userEntities;
    }

    @Override
    @JsonIgnore
    public Collection<? extends UserGroup> getUserGroups() {
        return userGroupEntities;
    }

    @Override
    @JsonIgnore
    public Collection<? extends Menu> getMenus() {
        return menuEntities;
    }

    @Override
    @JsonIgnore
    public Collection<? extends Resource> getResources() {
        return resourcesEntities;
    }

    @Override
    public void add(Collection<? extends Menu> menus) {
        if (null == menuEntities) {
            menuEntities = new ArrayList<>();
        }
        menuEntities.addAll(CollectionUtil.convert(menus));
    }

    @Override
    @SuppressWarnings({"SuspiciousMethodCalls"})
    public void remove(Collection<? extends Menu> menus) {
        if (null == menuEntities) {
            menuEntities = new ArrayList<>();
        }
        menuEntities.removeAll(CollectionUtil.convert(menus));
    }

    @Override
    public void addResources(Collection<? extends Resource> resources) {
        if (null == resourcesEntities) {
            resourcesEntities = new ArrayList<>();
        }
        resourcesEntities.addAll(CollectionUtil.convert(resources));
    }

    @Override
    @SuppressWarnings({"SuspiciousMethodCalls"})
    public void removeResources(Collection<? extends Resource> resources) {
        if (null == resourcesEntities) {
            resourcesEntities = new ArrayList<>();
        }
        resourcesEntities.removeAll(CollectionUtil.convert(resources));
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setParent(RoleEntity parent) {
        this.parent = parent;
    }

    public Collection<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(Collection<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public Collection<UserGroupEntity> getUserGroupEntities() {
        return userGroupEntities;
    }

    public void setUserGroupEntities(Collection<UserGroupEntity> userGroupEntities) {
        this.userGroupEntities = userGroupEntities;
    }

    public Collection<MenuEntity> getMenuEntities() {
        return menuEntities;
    }

    public void setMenuEntities(Collection<MenuEntity> menuEntities) {
        this.menuEntities = menuEntities;
    }

    public Collection<ResourceEntity> getResourcesEntities() {
        return resourcesEntities;
    }

    public void setResourcesEntities(Collection<ResourceEntity> resourcesEntities) {
        this.resourcesEntities = resourcesEntities;
    }
}
