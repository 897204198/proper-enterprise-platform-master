package com.proper.enterprise.platform.auth.neo4j.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.auth.neo4j.converter.GraphDataDicLiteConverter;
import com.proper.enterprise.platform.core.neo4j.entity.BaseNodeEntity;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = "PEP_AUTH_MENUS")
public class MenuNodeEntity extends BaseNodeEntity implements Menu {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuNodeEntity.class);

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
     * 描述说明
     */
    private String description;

    @Transient
    private boolean boot;

    /**
     * 菜单类别数据字典
     */
    @Convert(GraphDataDicLiteConverter.class)
    private DataDicLite menuType;

    /**
     * 菜单状态
     */
    private boolean enable = true;

    /**
     * 标识
     */
    private String identifier;

    @Transient
    private String parentId;

    private String menuCode;

    /**
     * 父菜单
     */
    @Relationship(type = "parent_to")
    private MenuNodeEntity parent;

    @Relationship(type = "parent_to", direction = Relationship.INCOMING)
    private Set<MenuNodeEntity> children = new HashSet<>();

    @Relationship(type = "has_resource")
    private Set<ResourceNodeEntity> resources = new HashSet<>();

    @Relationship(type = "has_menu", direction = Relationship.INCOMING)
    private Set<RoleNodeEntity> roles = new HashSet<>();

    public MenuNodeEntity(String id, String name, String route, String icon, MenuNodeEntity parent, int sequenceNumber) {
        this.id = id;
        this.name = name;
        this.route = route;
        this.icon = icon;
        this.parent = parent;
        this.sequenceNumber = sequenceNumber;
    }

    public MenuNodeEntity() {
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
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public boolean isBoot() {
        return boot;
    }

    public void setBoot(boolean boot) {
        this.boot = boot;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonIgnore
    @Override
    public Menu getParent() {
        return parent;
    }

    @Override
    public void setParent(Menu parent) {
        if (parent instanceof MenuNodeEntity) {
            this.parent = (MenuNodeEntity) parent;
        } else {
            LOGGER.error("Parent of a Menu SHOULD BE MenuEntity type, but get {} here.",
                    parent.getClass().getCanonicalName());
        }
    }

    @Override
    public String getParentId() {
        return parent == null ? null : parent.getId();
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @JsonIgnore
    @Override
    public Menu getApplication() {
        return getRootMenu(this);
    }

    private Menu getRootMenu(Menu self) {
        Menu parent = self.getParent();
        return parent == null ? self : getRootMenu(parent);
    }

    @Override
    public boolean isRoot() {
        return getParent() == null;
    }

    @JsonIgnore
    @Override
    public boolean isLeaf() {
        return getChildren().isEmpty();
    }

    @JsonIgnore
    @Override
    public Collection<? extends Menu> getChildren() {
        return children;
    }

    @Override
    public void addChild(Menu menu) {
        this.children.add((MenuNodeEntity) menu);
    }

    @Override
    public void removeChild(Menu menu) {
        this.children.remove(menu);
    }

    public void setChildren(Set<MenuNodeEntity> children) {
        this.children = children;
    }

    @JsonIgnore
    @Override
    public Collection<? extends Resource> getResources() {
        return resources;
    }

    @Override
    public void add(Resource resource) {
        this.resources.add((ResourceNodeEntity) resource);
    }

    public void add(RoleNodeEntity roleNodeEntity) {
        this.roles.add(roleNodeEntity);
    }

    @Override
    public void remove(Resource resource) {
        this.resources.remove(resource);
    }

    public void remove(RoleNodeEntity roleNodeEntity) {
        this.roles.remove(roleNodeEntity);
    }

    public void setResources(Set<ResourceNodeEntity> resources) {
        this.resources = resources;
    }

    @JsonIgnore
    @Override
    public Collection<? extends Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleNodeEntity> roles) {
        this.roles = roles;
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
    public String getMenuCode() {
        return menuCode;
    }

    @Override
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

}
