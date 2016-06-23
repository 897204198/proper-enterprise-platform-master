package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.api.auth.enums.PermissionType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限，描述菜单的权限类型（可多种）
 */
public class Permission {

    private Menu menu;

    private Set<PermissionType> types = new HashSet<>();

    /**
     * 根据菜单和权限类型构造一个权限
     *
     * @param menu  菜单
     * @param types 权限类型数组
     */
    public Permission(Menu menu, PermissionType... types) {
        this.menu = menu;
        Collections.addAll(this.types, types);
    }

    /**
     * 获得该权限所描述的菜单
     *
     * @return 菜单
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * 是否拥有某种权限
     *
     * @param type 要检测的权限
     * @return true：拥有；false：没有
     */
    public boolean could(PermissionType type) {
        return types.contains(type);
    }

}
