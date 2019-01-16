package com.proper.enterprise.platform.auth.common.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.view.BaseView;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;

import java.util.Collection;

@POJORelevance(relevanceDOClassName = "com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity")
public class MenuVO extends BaseVO implements Menu {

    public interface Single extends BaseView {

    }

    public interface MenuWithResource extends Single, ResourceVO.Single {

    }

    public MenuVO() {
    }

    @JsonView(value = {Single.class})
    private Boolean leaf;


    @JsonView(value = {Single.class})
    private MenuVO application;

    /**
     * 菜单名称
     */
    @JsonView(value = {Single.class})
    private String name;

    /**
     * 前端路径
     */
    @JsonView(value = {Single.class})
    private String route;

    /**
     * 父菜单
     */
    @JsonView(value = {MenuWithResource.class})
    private MenuVO parent;

    /**
     * 上级菜单 id
     */
    @JsonView(value = {Single.class})
    private String parentId;

    /**
     * 菜单类型编码
     */
    @JsonView(value = {Single.class})
    private String menuCode;

    /**
     * 子菜单集合
     */
    @JsonView(value = {MenuWithResource.class})
    private Collection<MenuVO> children;

    private Collection<RoleVO> roles;

    @JsonView(value = {MenuWithResource.class})
    private Collection<ResourceVO> resources;

    /**
     * 菜单同级排序号
     */
    @JsonView(value = {Single.class})
    private Integer sequenceNumber;

    /**
     * 菜单图标样式名称
     */
    @JsonView(value = {Single.class})
    private String icon;

    /**
     * 描述说明
     */
    @JsonView(value = {Single.class})
    private String description;

    /**
     * 菜单类别数据字典
     */
    @JsonView(value = {Single.class})
    private DataDicLite menuType;

    /**
     * 标识
     */
    @JsonView(value = {Single.class})
    private String identifier;

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
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
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
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
    public MenuVO getParent() {
        return parent;
    }

    public void setParent(MenuVO parent) {
        this.parent = parent;
    }

    @Override
    public void addParent(Menu parent) {
        if (parent == null) {
            this.parent = null;
            return;
        }
        this.parent = (MenuVO) parent;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public MenuVO getApplication() {
        return application;
    }

    public void setApplication(MenuVO application) {
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
        return DataDicUtil.convert(menuType);
    }

    @Override
    public void setMenuType(DataDicLite menuType) {
        this.menuType = menuType;
    }

    @Override
    public Collection<MenuVO> getChildren() {
        return children;
    }

    @Override
    public Collection<RoleVO> getRoles() {
        return roles;
    }

    @Override
    public Collection<ResourceVO> getResources() {
        return resources;
    }

    @Override
    public String getMenuCode() {
        return menuCode;
    }

    @Override
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public void setChildren(Collection<MenuVO> children) {
        this.children = children;
    }

    public void setRoles(Collection<RoleVO> roles) {
        this.roles = roles;
    }

    public void setResources(Collection<ResourceVO> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
