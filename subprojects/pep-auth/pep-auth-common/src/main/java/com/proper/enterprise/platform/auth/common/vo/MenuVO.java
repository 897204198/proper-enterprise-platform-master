package com.proper.enterprise.platform.auth.common.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.convert.annotation.POJOConverter;
import com.proper.enterprise.platform.core.convert.annotation.POJORelevance;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.view.BaseView;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;

import java.util.Collection;

@POJORelevance(relevanceDOClassName = "com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity")
public class MenuVO extends BaseVO implements Menu {

    private static final String MENU_ENTITY_PATH = "com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity";

    public interface Single extends BaseView {

    }

    public interface MenuWithResource extends Single, ResourceVO.Single {

    }

    public MenuVO() {
    }

    @JsonView(value = {Single.class})
    private boolean leaf;


    @JsonView(value = {Single.class})
    private Menu application;

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
    @POJOConverter(fromClassName = MENU_ENTITY_PATH,
        fieldName = "parent",
        targetClassName = MENU_ENTITY_PATH)
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
    @POJOConverter(fromClassName = MENU_ENTITY_PATH,
        fieldName = "children",
        targetClassName = MENU_ENTITY_PATH)
    private Collection<MenuVO> children;

    @POJOConverter(fromClassName = MENU_ENTITY_PATH,
        fieldName = "roleEntities",
        targetClassName = MENU_ENTITY_PATH)
    private Collection<RoleVO> roles;

    @POJOConverter(fromClassName = MENU_ENTITY_PATH,
        fieldName = "resourceEntities",
        targetClassName = MENU_ENTITY_PATH)
    @JsonView(value = {MenuWithResource.class})
    private Collection<ResourceVO> resources;

    /**
     * 菜单同级排序号
     */
    @JsonView(value = {Single.class})
    private int sequenceNumber;

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
        this.leaf = this.isLeaf();
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
        if (CollectionUtil.isEmpty(getChildren()) && CollectionUtil.isEmpty(resources)) {
            return true;
        }
        return false;
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
}
