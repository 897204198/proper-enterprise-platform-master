package com.proper.enterprise.platform.auth.common.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@CacheEntity
@Table(name = "PEP_AUTH_MENUS")
public class MenuEntity extends BaseEntity implements Menu {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuEntity.class);

    public MenuEntity() {
    }

    /**
     * 菜单名称
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * 前端路径
     */
    @Column(unique = true, nullable = false)
    private String route;

    /**
     * 菜单同级排序号
     */
    private Integer sequenceNumber;

    /**
     * 菜单图标样式名称
     */
    private String icon;

    /**
     * 描述说明
     */
    private String description;

    /**
     * 菜单类别数据字典
     */
    @Convert(converter = DataDicLiteConverter.class)
    private DataDicLite menuType;

    /**
     * 标识
     */
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String identifier;

    /**
     * 父菜单
     */
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    @JsonIgnore
    private MenuEntity parent;

    /**
     * 子菜单集合
     */
    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private Collection<MenuEntity> children = new ArrayList<>();

    /**
     * 菜单关联的资源集合
     */
    @ManyToMany
    @JoinTable(name = "PEP_AUTH_MENUS_RESOURCES",
        joinColumns = @JoinColumn(name = "MENU_ID"),
        inverseJoinColumns = @JoinColumn(name = "RES_ID"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"MENU_ID", "RES_ID"}))
    private Collection<ResourceEntity> resourceEntities = new ArrayList<>();

    /**
     * 拥有此菜单权限的角色集合
     */
    @ManyToMany(mappedBy = "menuEntities")
    private Collection<RoleEntity> roleEntities = new ArrayList<>();

    @Transient
    private String parentId;

    private String menuCode;

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
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
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
    public void addParent(Menu parent) {
        if (null == parent) {
            this.parent = null;
            return;
        }
        if (parent instanceof MenuEntity) {
            this.parent = (MenuEntity) parent;
        } else {
            LOGGER.error("Parent of a Menu SHOULD BE MenuEntity type, but get {} here.",
                parent.getClass().getCanonicalName());
        }
    }

    @Override
    public String getParentId() {
        return parentId;
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
        if (null == children) {
            children = new ArrayList<>();
        }
        children.add((MenuEntity) menu);
    }

    @Override
    public void removeChild(Menu menu) {
        if (null == children) {
            return;
        }
        children.remove(menu);
    }

    @Override
    public void add(Resource resource) {
        if (null == resourceEntities) {
            resourceEntities = new ArrayList<>();
        }
        resourceEntities.add((ResourceEntity) resource);
    }

    @Override
    public void remove(Resource resource) {
        if (null == resourceEntities) {
            return;
        }
        resourceEntities.remove(resource);
    }

    @Override
    public boolean isRoot() {
        return getParent() == null;
    }

    @Override
    public boolean isLeaf() {
        if (null == getChildren()) {
            return true;
        }
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

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public DataDicLite getMenuType() {
        return menuType;
    }

    @Override
    public void setMenuType(DataDicLite menuType) {
        this.menuType = menuType;
    }

    @Override
    @JsonIgnore
    public Collection<? extends Role> getRoles() {
        return roleEntities;
    }

    @Override
    @JsonIgnore
    public Collection<? extends Resource> getResources() {
        return resourceEntities;
    }

    @Override
    public String getMenuCode() {
        return menuCode;
    }

    @Override
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public void setParent(MenuEntity parent) {
        this.parent = parent;
    }

    public void setChildren(Collection<MenuEntity> children) {
        this.children = children;
    }

    public Collection<ResourceEntity> getResourceEntities() {
        return resourceEntities;
    }

    public void setResourceEntities(Collection<ResourceEntity> resourceEntities) {
        this.resourceEntities = resourceEntities;
    }

    public Collection<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(Collection<RoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
