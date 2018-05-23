package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;

import java.util.Collection;

/**
 * 用户组
 * <p>
 * 用来将用户组织到一起，方便被引用
 * 删除用户组时需考虑用户组引用关系
 */
public interface UserGroup extends IBase {

    /**
     * 获得用户组名称
     *
     * @return 用户组名称
     */
    String getName();

    /**
     * 设置用户组名称
     *
     * @param name 用户组名称
     */
    void setName(String name);

    /**
     * 获得用户组描述信息
     *
     * @return 用户组描述信息
     */
    String getDescription();

    /**
     * 设置用户组描述信息
     *
     * @param description 描述信息
     */
    void setDescription(String description);

    /**
     * 获得顺序
     *
     * @return 顺序
     */
    int getSeq();

    /**
     * 设定顺序
     *
     * @param seq 顺序
     */
    void setSeq(int seq);

    /**
     * 向用户组中添加一个用户
     *
     * @param user 用户
     */
    void add(User user);

    /**
     * 向用户组添加用户
     *
     * @param users 用户集合
     */
    void add(User... users);

    /**
     * 向用户组中添加一个角色
     *
     * @param role 角色
     */
    void add(Role role);

    /**
     * 从组中移除一个用户
     *
     * @param user 用户
     */
    void remove(User user);

    /**
     * 从组中移除一个角色
     *
     * @param role 角色
     */
    void remove(Role role);

    /**
     * 从组中移除全部用户
     */
    void removeAllUsers(Collection<? extends User> users);

    /**
     * 获得用户组下用户集合
     *
     * @return 用户集合
     */
    Collection<? extends User> getUsers();

    /**
     * 获得用户组下角色集合
     *
     * @return 角色集合
     */
    Collection<? extends Role> getRoles();

}
