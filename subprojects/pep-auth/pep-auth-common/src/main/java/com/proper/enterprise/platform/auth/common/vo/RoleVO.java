package com.proper.enterprise.platform.auth.common.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.core.convert.annotation.POJOConverter;
import com.proper.enterprise.platform.core.convert.annotation.POJORelevance;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.view.BaseView;

import java.util.Collection;

@POJORelevance(relevanceDOClassName = "com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity")
public class RoleVO extends BaseVO implements Role {

    public interface Single extends BaseView {

    }

    public interface RoleWithMenu extends Single, MenuVO.Single {

    }

    public interface RoleWithResource extends Single, ResourceVO.Single {

    }

    public RoleVO() {
    }

    private static final String ROLE_ENTITY_PATH = "com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity";
    /**
     * 名称
     */
    @JsonView(value = {UserGroupVO.GroupWithRole.class, UserVO.CurrentUser.class, Single.class})
    private String name;

    /**
     * 权限描述UserEntity
     */
    @JsonView(value = {UserGroupVO.GroupWithRole.class, Single.class})
    private String description;

    /**
     * 父菜单
     */
    @POJOConverter(fromClassName = ROLE_ENTITY_PATH,
        fieldName = "parent",
        targetClassName = ROLE_ENTITY_PATH)
    @JsonView(value = UserGroupVO.GroupWithRole.class)
    private RoleVO parent;

    @POJOConverter(fromClassName = ROLE_ENTITY_PATH,
        fieldName = "parent", fromHandleBy = RoleVoFromHandler.class)
    @JsonView(value = {UserGroupVO.GroupWithRole.class, Single.class})
    private String parentId;

    @JsonView(value = {UserGroupVO.GroupWithRole.class, Single.class})
    private String parentName;

    @POJOConverter(fromClassName = ROLE_ENTITY_PATH,
        fieldName = "menuEntities",
        targetClassName = ROLE_ENTITY_PATH)
    private Collection<MenuVO> menus;

    @POJOConverter(fromClassName = ROLE_ENTITY_PATH,
        fieldName = "userEntities",
        targetClassName = ROLE_ENTITY_PATH)
    private Collection<UserVO> users;

    @POJOConverter(fromClassName = ROLE_ENTITY_PATH,
        fieldName = "userGroupEntities",
        targetClassName = ROLE_ENTITY_PATH)
    private Collection<UserGroupVO> userGroups;

    @POJOConverter(fromClassName = ROLE_ENTITY_PATH,
        fieldName = "resourcesEntities",
        targetClassName = ROLE_ENTITY_PATH)
    private Collection<ResourceVO> resources;

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
        return obj != null && (obj instanceof RoleVO) && id.equals(((RoleVO) obj).id);
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

    public void setParent(RoleVO parent) {
        this.parent = parent;
    }

    @Override
    public void addParent(Role parent) {
        if (parent instanceof RoleVO) {
            this.parent = (RoleVO) parent;
        }
    }

    @Override
    public Collection<MenuVO> getMenus() {
        return menus;
    }

    @Override
    public Collection<UserVO> getUsers() {
        return users;
    }


    @Override
    public Collection<UserGroupVO> getUserGroups() {
        return userGroups;
    }


    @Override
    public Collection<ResourceVO> getResources() {
        return resources;
    }

    @Override
    public void add(Collection<? extends Menu> menus) {
    }

    @Override
    public void remove(Collection<? extends Menu> menus) {
    }

    @Override
    public void addResources(Collection<? extends Resource> resources) {
    }

    @Override
    public void removeResources(Collection<? extends Resource> resources) {
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setResources(Collection<ResourceVO> resources) {
        this.resources = resources;
    }

    public void setMenus(Collection<MenuVO> menus) {
        this.menus = menus;
    }

    public void setUsers(Collection<UserVO> users) {
        this.users = users;
    }

    public void setUserGroups(Collection<UserGroupVO> userGroups) {
        this.userGroups = userGroups;
    }
}
