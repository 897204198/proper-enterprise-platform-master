package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;

import java.util.Collection;

/**
 * 用户
 * <p>
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
     * 获得显示名称
     *
     * @return 显示名称
     */
    String getName();

    /**
     * 设置显示名称
     *
     * @param name 显示名称
     */
    void setName(String name);

    /**
     * 获得手机号
     *
     * @return 手机号
     */
    String getPhone();

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    void setPhone(String phone);

    /**
     * 为用户增加一个角色
     *
     * @param role 角色
     */
    void add(Role role);

    /**
     * 为用户增加一个用户组
     *
     * @param userGroup 用户组
     */
    void add(UserGroup userGroup);

    /**
     * 为用户移除一个角色
     *
     * @param role 角色
     */
    void remove(Role role);

    /**
     * 为用户移除一个用户组
     *
     * @param userGroup 用户组
     */
    void remove(UserGroup userGroup);

    /**
     * 判断用户是否为超级用户
     * 超级用户拥有系统内所有资源的使用和分配权限
     *
     * @return 返回 true 时代表是超级用户
     */
    Boolean getSuperuser();

    /**
     * 设置是否为超级用户
     *
     * @param superuser true 是 false否
     */
    void setSuperuser(Boolean superuser);

    /**
     * 获得用户所属的用户组
     *
     * @return 用户组集合
     */
    Collection<? extends UserGroup> getUserGroups();

    /**
     * 获得用户拥有的角色集合
     *
     * @return 角色集合
     */
    Collection<? extends Role> getRoles();

    /**
     * 获得头像
     *
     * @return 头像Id
     */
    String getAvatar();

    /**
     * 设置头像
     *
     * @param avatar 头像Id
     */
    void setAvatar(String avatar);

}
