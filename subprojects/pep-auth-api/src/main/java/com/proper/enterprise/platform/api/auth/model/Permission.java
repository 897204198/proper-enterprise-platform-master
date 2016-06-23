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

    public Permission(Menu menu, PermissionType... types) {
        this.menu = menu;
        Collections.addAll(this.types, types);
    }

    public Menu getMenu() {
        return menu;
    }

    public boolean could(PermissionType type) {
        return types.contains(type);
    }

}
