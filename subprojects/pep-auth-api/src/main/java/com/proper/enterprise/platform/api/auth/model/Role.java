package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;

/**
 * 角色
 *
 * 角色定义应用的使用权限，权限控制至菜单下`资源`。
 * 角色以树形结构组织，角色`可继承`，子角色权限范围是父角色权限范围的子集。
 *
 * 权限分为两种类型：
 *  - `可分配`：用户只能将该资源进行分配，不能在系统中使用该资源代表的功能。
 *  - `可使用`：用户可以在系统中见到和使用该资源代表的功能，但不能将该资源分配给其他用户/角色。
 *
 *  角色和用户的关系可通过`角色规则`进行设定，默认无角色规则，可从用户方面选择未设置角色规则的角色。
 */
public interface Role extends IBase {

    String getName();

    void setName(String name);

    Role getParent();

    void setParent(Role role);

    Set<Permission> getPermissions();



}
