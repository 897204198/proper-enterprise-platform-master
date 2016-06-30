package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;

import java.util.Collection;

/**
 * 用户
 *
 * 用户即系统中的账号，需要设定用户名、密码及邮箱。
 * 系统账号来源应支持多种形式，以适应单点登录需求。
 * 根据角色规则，可查看用户所拥有的角色，可为用户分配没有角色规则的普通角色。
 */
public interface User extends IBase {

    /**
     * 获得用户名
     *
     * @return 用户名
     */
    String getUsername();

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    void setUsername(String username);

    /**
     * 获得密码
     *
     * @return 密码
     */
    String getPassword();

    /**
     * 设置密码
     *
     * @param password 密码
     */
    void setPassword(String password);

    /**
     * 获得邮箱
     *
     * @return 邮箱
     */
    String getEmail();

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    void setEmail(String email);

    /**
     * 获得用户拥有的角色集合
     *
     * @return 角色集合
     */
    Collection<? extends Role> getRoles();

    /**
     * 为用户增加一个角色
     *
     * @param role 角色
     */
    void addRole(Role role);

    /**
     * 为用户移除一个角色
     *
     * @param role 角色
     */
    void removeRole(Role role);

}
