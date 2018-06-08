package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.core.entity.DataTrunk;

import java.util.Collection;
import java.util.List;

/**
 * 用户服务接口
 * <p>
 * 用户名（username）作为用户唯一标识，可为用户所见
 * 用户ID（userId）同样为用户唯一标识，但对用户透明，仅在系统内部使用
 */
public interface UserService {

    /**
     * 保存用户
     *
     * @param user 保存用户信息
     * @return 用户信息
     */
    User save(User user);

    /**
     * 保存用户
     *
     * @param users 保存用户集合
     */
    void save(User... users);

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 是否删除
     */
    boolean delete(String id);

    /**
     * 批量删除用户
     *
     * @param ids 以 , 分隔的待删除用户ID列表
     * @return 是否删除
     */
    boolean deleteByIds(String ids);

    /**
     * 更新用户
     *
     * @param user 更新用户信息
     * @return 用户细信息
     */
    User update(User user);

    /**
     * 修改密码
     *
     * @param userId      用户id
     * @param oldPassword 原密码
     * @param password    新密码
     * @return 用户信息
     */
    User changePassword(String userId, String oldPassword, String password);

    /**
     * 获取当前用户
     * 用户为启用状态
     *
     * @return 用户信息
     */
    User getCurrentUser();

    /**
     * 根据Id获取用户
     * 启用+停用
     *
     * @param id 用户id
     * @return 用户信息
     */
    User get(String id);

    /**
     * 根据用户账号获取用户
     *
     * @param username   用户账号
     * @param enableEnum 是否启用
     * @return 用户信息
     */
    User getByUsername(String username, EnableEnum enableEnum);

    /**
     * 通过用户id集合查询用户
     *
     * @param ids 用户id集合
     * @return 用户列表
     */
    Collection<? extends User> getUsersByIds(List<String> ids);

    /**
     * 按照查询条件获取用户信息列表 ||
     *
     * @param condition 用户名 or 用户显示名 or 用户邮箱 or 用户手机号
     * @param enable    用户可用/不可用
     * @return 用户信息列表
     */
    Collection<? extends User> getUsersByOrCondition(String condition, EnableEnum enable);

    /**
     * 按照查询条件获取用户信息列表 &&
     *
     * @param userName 用户名
     * @param name     用户显示名
     * @param email    用户邮箱
     * @param phone    用户手机号
     * @param enable   用户可用/不可用
     * @return 用户信息列表
     */
    Collection<? extends User> getUsersByAndCondition(String userName, String name, String email, String phone, EnableEnum enable);

    /**
     * 按照查询条件获取用户信息列表 &&
     *
     * @param userName 用户名
     * @param name     用户显示名
     * @param email    用户邮箱
     * @param phone    用户手机号
     * @param enable   用户可用/不可用/全部
     * @return 用户信息列表 分页
     */
    DataTrunk<? extends User> findUsersPagniation(String userName, String name, String email, String phone, EnableEnum enable);

    /**
     * 更新启用禁用
     *
     * @param idList 用户id集合
     * @param enable 是否启用
     * @return 更新用的用户集合
     */
    Collection<? extends User> updateEnable(Collection<String> idList, boolean enable);

    /**
     * 添加用户权限
     *
     * @param userId 用户ID
     * @param roleId 权限ID
     * @return 用户信息
     */
    User addUserRole(String userId, String roleId);

    /**
     * 移除用户权限
     *
     * @param userId 用户ID
     * @param roleId 权限ID
     * @return 用户信息(添加成功返回用户信息, 失败返回null)
     */
    User deleteUserRole(String userId, String roleId);


    /**
     * 获得当前登录用户权限范围内菜单集合
     *
     * @return 菜单集合
     */
    Collection<? extends Menu> getUserMenus(String userId, EnableEnum menuEnable);

    /**
     * 获得某用户所拥有的资源集合
     *
     * @param userId         用户 ID
     * @param resourceEnable 资源是否启用
     * @return 资源集合
     */
    Collection<? extends Resource> getUserResources(String userId, EnableEnum resourceEnable);

    /**
     * 获取指定用户的用户组集合
     *
     * @param userId 用户ID
     * @return 用户组集合
     */
    Collection<? extends UserGroup> getUserGroups(String userId);

    /**
     * 获取指定用户的用户组集合
     *
     * @param userId          用户ID
     * @param userGroupEnable 角色默认为启用
     * @return 用户组集合
     */
    Collection<? extends UserGroup> getUserGroups(String userId, EnableEnum userGroupEnable);

    /**
     * 获取指定用户角色集合
     *
     * @param userId 用户ID
     * @return 角色集合
     */
    Collection<? extends Role> getUserRoles(String userId);

    /**
     * 获取指定用户角色集合
     *
     * @param userId     用户ID
     * @param roleEnable 角色默认为启用
     * @return 角色集合
     */
    Collection<? extends Role> getUserRoles(String userId, EnableEnum roleEnable);

    /**
     * 根据传入的用户集合，获取合法的用户集合(过滤掉valid、enable为false的)
     *
     * @param users 待检测的用户集合
     * @return 返回合法的用户集合
     */
    Collection<? extends User> getFilterUsers(Collection<? extends User> users, EnableEnum userEnable);

}
