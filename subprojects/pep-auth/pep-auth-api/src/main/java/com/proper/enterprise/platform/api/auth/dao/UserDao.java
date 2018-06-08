package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

public interface UserDao extends BaseService<User, String> {

    User save(User user);

    void save(User... users);

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
     * 根据userId集合删除所有
     *
     * @param ids userId集合
     * @return 是否成功删除
     */
    boolean deleteByIds(Collection<String> ids);


    User updateForSelective(User user);

    User getNewUser();

    User getByUsername(String username, EnableEnum enableEnum);

    User getCurrentUserByUserId(String userId);

    Collection<? extends User> findAll(Collection<String> idList);

    Collection<? extends User> getUsersByOrCondition(String condition, EnableEnum enable);

    Collection<? extends User> getUsersByAndCondition(String userName, String name, String email, String phone, EnableEnum enable);

    DataTrunk<? extends User> findUsersPagniation(String userName, String name, String email, String phone, EnableEnum enable);

    User changePassword(String userId, String oldPassword, String password);
}
