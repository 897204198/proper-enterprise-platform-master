package com.proper.enterprise.platform.auth.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="PEP_AUTH_MENUS")
@CacheEntity
public class MenuEntity extends BaseEntity implements Menu {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuEntity.class);

    public MenuEntity() { }

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 前端路径
     */
    private String route;

    /**
     * 菜单同级排序号
     */
    private int sequenceNumber;

    /**
     * 菜单图标样式名称
     */
    private String icon;

    /**
     * 父菜单
     */
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    @JsonIgnore
    private MenuEntity parent;

    @Transient
    private String parentId;

    /**
     * 子菜单集合
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    @JsonIgnore
    private Collection<MenuEntity> children = new ArrayList<>();

    /**
     * 菜单关联的资源集合
     */
    @ManyToMany
    @JoinTable(name = "PEP_AUTH_MENUS_RESOURCES",
        joinColumns = @JoinColumn(name = "MENU_ID"),
        inverseJoinColumns = @JoinColumn(name = "RES_ID"))
    private Collection<ResourceEntity> resourceEntities = new ArrayList<>();

    /**
     * 拥有此菜单权限的角色集合
     */
    @ManyToMany(mappedBy = "menuEntities")
    private Collection<RoleEntity> roleEntities = new ArrayList<>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getRoute() {
        return route;
    }

    @Override
    public void setRoute(String route) {
        this.route = route;
    }

    @Override
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public Menu getParent() {
        return parent;
    }

    @Override
    public void setParent(Menu parent) {
        if (parent instanceof MenuEntity) {
            this.parent = (MenuEntity) parent;
        } else {
            LOGGER.error("Parent of a Menu SHOULD BE MenuEntity type, but get {} here.",
                parent.getClass().getCanonicalName());
        }
    }

    @Override
    public String getParentId() {
        return parent == null ? null : parent.getId();
    }

    @Override
    @JsonIgnore
    public Menu getApplication() {
        return getRootMenu(this);
    }

    private Menu getRootMenu(Menu self) {
        Menu parent = self.getParent();
        return parent == null ? self : getRootMenu(parent);
    }

    @Override
    public Collection<? extends Menu> getChildren() {
        return this.children;
    }

    @Override
    public void addChild(Menu menu) {
        children.add((MenuEntity) menu);
    }

    @Override
    public void removeChild(Menu menu) {
        children.remove(menu);
    }

    @Override
    @JsonIgnore
    public Collection<? extends Resource> getResources() {
        return resourceEntities;
    }

    @Override
    public void add(Resource resource) {
        resourceEntities.add((ResourceEntity) resource);
    }

    @Override
    public void remove(Resource resource) {
        resourceEntities.remove(resource);
    }

    @Override
    @JsonIgnore
    public Collection<? extends Role> getRoles() {
        return roleEntities;
    }

    @Override
    public boolean isRoot() {
        return getParent() == null;
    }

    @Override
    @JsonIgnore
    public boolean isLeaf() {
        return getChildren().isEmpty();
    }

    @Override
    public int compareTo(Menu o) {
        int result = id.compareTo(o.getId());
        if (result == 0) {
            result = sequenceNumber - o.getSequenceNumber();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Menu && this.compareTo((Menu) obj) == 0;
    }

    @Override
    public int hashCode() {
        return (id + sequenceNumber).hashCode();
    }

}
