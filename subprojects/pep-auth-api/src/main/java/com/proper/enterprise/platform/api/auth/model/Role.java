package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;

import java.util.Collection;
import java.util.Set;

/**
 * 角色
 *
 * 角色定义应用的使用权限，权限控制至菜单叶子节点表示的`资源`。
 * 角色以树形结构组织，角色`可继承`，子角色权限范围是父角色权限范围的子集。
 *
 * 权限分为两种类型：
 *  - `可分配`：用户只能将该资源进行分配，不能在系统中使用该资源代表的功能。
 *  - `可使用`：用户可以在系统中见到和使用该资源代表的功能，但不能将该资源分配给其他用户/角色。
 *
 * 角色和用户的关系可通过`角色规则`进行设定，默认无角色规则，可从用户方面选择未设置角色规则的角色。
 */
public interface Role extends IBase {

    /**
     * 获得角色名称
     *
     * @return 角色名称
     */
    String getName();

    /**
     * 设置角色名称
     *
     * @param name 角色名称
     */
    void setName(String name);

    /**
     * 获得上级角色
     *
     * @return 上级角色
     */
    Role getParent();

    /**
     * 设置上级角色
     *
     * @param role 上级角色
     */
    void setParent(Role role);

    /**
     * 获得角色权限集合
     *
     * @return 权限集合
     */
    Set<Permission> getPermissions();

    /**
     * 设置角色权限集合
     *
     * @param permissions 权限集合
     */
    void setPermission(Set<Permission> permissions);

    /**
     * 获得角色拥有的菜单集合
     *
     * @return
     */
    Collection<? extends Menu> getMenus();

}
