package com.proper.enterprise.platform.auth.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "PEP_AUTH_ROLES")
@CacheEntity
public class RoleEntity extends BaseEntity implements Role {

    public RoleEntity() { }

    /**
     * 名称
     */
    @Column
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
    @JoinColumn(name = "PARENT_ID")
    @JsonIgnore
    private RoleEntity parent;

    @Transient
    private String parentId;

    @Transient
    private Collection<? extends Menu> menus = new ArrayList<>();

    @Transient
    private Collection<? extends User> users = new ArrayList<>();

    @Transient
    private Collection<? extends UserGroup> userGroups = new ArrayList<>();

    @Transient
    private Collection<? extends Resource> resources = new ArrayList<>();

    /**
     * 用户状态
     */
    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private boolean enable = true;

    @ManyToMany(mappedBy = "roleEntities")
    private Collection<UserEntity> userEntities = new ArrayList<>();

    @ManyToMany(mappedBy = "roleGroupEntities")
    private Collection<UserGroupEntity> userGroupEntities = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_ROLES_MENUS",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "MENU_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"ROLE_ID", "MENU_ID"}))
    private Collection<MenuEntity> menuEntities = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_ROLES_RESOURCES",
        joinColumns = @JoinColumn(name = "ROLE_ID"),
        inverseJoinColumns = @JoinColumn(name = "RESOURCE_ID"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"ROLE_ID", "RESOURCE_ID"}))
    private Collection<ResourceEntity> resourcesEntities = new ArrayList<>();

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public Role getParent() {
        return parent;
    }

    @Override
    public void setParent(Role parent) {
        if (parent instanceof RoleEntity) {
            this.parent = (RoleEntity) parent;
        }
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
        menuEntities.addAll(CollectionUtil.convert(menus));
    }

    @Override
    @SuppressWarnings({"SuspiciousMethodCalls"})
    public void remove(Collection<? extends Menu> menus) {
        menuEntities.removeAll(CollectionUtil.convert(menus));
    }

    @Override
    public void addResources(Collection<? extends Resource> resources) {
        resourcesEntities.addAll(CollectionUtil.convert(resources));
    }

    @Override
    @SuppressWarnings({"SuspiciousMethodCalls"})
    public void removeResources(Collection<? extends Resource> resources) {
        resourcesEntities.removeAll(CollectionUtil.convert(resources));
    }
}
