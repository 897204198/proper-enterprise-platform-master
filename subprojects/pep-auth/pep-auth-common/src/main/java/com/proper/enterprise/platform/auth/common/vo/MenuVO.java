package com.proper.enterprise.platform.auth.common.vo;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;

import java.util.ArrayList;
import java.util.Collection;


public class MenuVO extends BaseVO implements Menu {

    private boolean leaf;

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    private Menu application;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 前端路径
     */
    private String route;

    /**
     * 父菜单
     */
    private Menu parent;

    /**
     * 上级菜单 id
     */
    private String parentId;

    /**
     * 菜单类型编码
     */
    private String menuCode;

    /**
     * 子菜单集合
     */
    private Collection<MenuVO> children = new ArrayList<>();

    private Collection<? extends Role> roles = new ArrayList<>();

    private Collection<? extends Resource> resources = new ArrayList<>();

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

    /**
     * 菜单类别数据字典
     */
    private DataDicLite menuType;

    /**
     * 菜单状态
     */
    private boolean enable = true;

    /**
     * 标识
     */
    private String identifier;

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
        if (parent != null) {
            this.parent = parent;
        }
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public Menu getApplication() {
        return application;
    }

    public void setApplication(Menu application) {
        this.application = application;
    }

    @Override
    public void addChild(Menu menu) {

    }

    @Override
    public void removeChild(Menu menu) {

    }

    @Override
    public void add(Resource resource) {

    }

    @Override
    public void remove(Resource resource) {

    }

    @Override
    public boolean isRoot() {
        return getParent() == null;
    }

    @Override
    public boolean isLeaf() {
        return leaf;
    }

    @Override
    public int compareTo(Menu o) {
        return 0;
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
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public Collection<MenuVO> getChildren() {
        return null;
    }

    @Override
    public Collection<? extends Role> getRoles() {
        return roles;
    }

    @Override
    public Collection<? extends Resource> getResources() {
        return resources;
    }

    public void setResources(Collection<? extends Resource> resources) {
        this.resources = resources;
    }

    @Override
    public String getMenuCode() {
        return menuCode;
    }

    @Override
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public void setRoles(Collection<? extends Role> roles) {
        this.roles = roles;
    }

    public void setChildren(Collection<MenuVO> children) {
        this.children = children;
    }
}
