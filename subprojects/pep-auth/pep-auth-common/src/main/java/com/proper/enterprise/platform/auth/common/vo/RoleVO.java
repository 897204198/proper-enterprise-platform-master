package com.proper.enterprise.platform.auth.common.vo;

import com.proper.enterprise.platform.api.auth.model.*;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;

public class RoleVO extends BaseVO implements Role {
    /**
     * 名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 父菜单
     */
    private RoleVO parent;

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
    private boolean enable = true;

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
        if (parent instanceof RoleVO) {
            this.parent = (RoleVO) parent;
        }
    }

    @Override
    public Collection<? extends Menu> getMenus() {
        return menus;
    }

    @Override
    public Collection<? extends User> getUsers() {
        return users;
    }


    @Override
    public Collection<? extends UserGroup> getUserGroups() {
        return userGroups;
    }


    @Override
    public Collection<? extends Resource> getResources() {
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
}
