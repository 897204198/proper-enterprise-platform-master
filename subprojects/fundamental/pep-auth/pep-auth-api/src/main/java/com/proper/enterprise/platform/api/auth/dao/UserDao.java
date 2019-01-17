package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

@SuppressWarnings("unchecked")
public interface UserDao extends BaseService<User, String> {

    /**
     * 添加用户
     *
     * @param user 用户
     * @return 用户
     */
    User save(User user);

    /**
     * 添加多个用户
     *
     * @param users 多个用户
     * @return 保存用户集合
     */
    User[] save(User... users);

    /**
     * 添加用户权限
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 用户
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
     * 根据userId集合删除所有
     *
     * @param ids userId集合
     * @return 是否成功删除
     */
    boolean deleteByIds(Collection<String> ids);

    /**
     * 修改用户信息
     *
     * @param user 更新的用户实体.
     * @return 修改后的对象
     */
    User updateForSelective(User user);

    /**
     * 创建空的用户对象
     *
     * @return 新的用户实体
     */
    User getNewUser();

    /**
     * 通过用户名获取用户信息
     *
     * @param username   用户名
     * @param enableEnum 是否可用
     * @return 用户
     */
    User getByUsername(String username, EnableEnum enableEnum);

    /**
     * 获取当前用户
     *
     * @param userId 用户id
     * @return 用户
     */
    User getCurrentUserByUserId(String userId);

    /**
     * 获取多个用户
     *
     * @param idList 多个用户id
     * @return 用户集合
     */
    Collection<? extends User> findAll(Collection<String> idList);

    /**
     * 通过不同条件模糊查询
     *
     * @param condition 条件
     * @param enable    是否可用
     * @return 用户集合
     */
    Collection<? extends User> getUsersByOrCondition(String condition, EnableEnum enable);

    /**
     * 多条件模糊查询
     *
     * @param userName 账号
     * @param name     用户名称
     * @param email    邮箱
     * @param phone    手机号
     * @param enable   是否可用
     * @return 用户集合
     */
    Collection<? extends User> getUsersByAndCondition(String userName, String name, String email, String phone, EnableEnum enable);

    /**
     * 分页多条件模糊查询
     *
     * @param userName 账号
     * @param name     用户名称
     * @param email    邮箱
     * @param phone    手机
     * @param enable   是否可用
     * @return 用户集合
     */
    DataTrunk<? extends User> findUsersPagination(String userName, String name, String email, String phone, EnableEnum enable);

    /**
     * 用户修改密码
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param password    新密码
     * @return 用户
     */
    User updateChangePassword(String userId, String oldPassword, String password);

    /**
     * 用户重置密码
     *
     * @param userId   用户id
     * @param password 新密码
     * @return 用户
     */
    User updateResetPassword(String userId, String password);
}
