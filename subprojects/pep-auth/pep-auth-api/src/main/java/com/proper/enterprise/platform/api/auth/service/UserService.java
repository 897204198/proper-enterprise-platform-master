package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.entity.DataTrunk;

import java.util.Collection;

/**
 * 用户服务接口
 *
 * 用户名（username）作为用户唯一标识，可为用户所见
 * 用户ID（userId）同样为用户唯一标识，但对用户透明，仅在系统内部使用
 */
public interface UserService {

    User save(User user);

    void save(User... users);

    User getCurrentUser();

    User get(String id);

    User getByUsername(String username);

    void delete(String id);

    void delete(User user);

    /**
     * 获得当前登录用户权限范围内菜单集合
     *
     * @return 菜单集合
     */
    Collection<? extends Menu> getMenus();

    Collection<? extends Menu> getMenus(String userId);

    Collection<? extends Menu> getMenusByUsername(String username);

    /**
     * 按照查询条件获取用户信息列表
     *
     * @param userName 用户名
     * @param name     用户显示名
     * @param email    用户邮箱
     * @param phone    用户手机号
     * @param enable   用户可用/不可用
     * @param pageNo   分页页码
     * @param pageSize 分页大小
     * @return 用户信息列表
     */
    DataTrunk<? extends User> getUsersByCondiction(String userName, String name, String email, String phone, String enable,
                                                    Integer pageNo, Integer pageSize);

    /**
     * 删除多条用户数据
     *
     * @param ids 以 , 分隔的待删除用户ID列表
     */
    boolean deleteByIds(String ids);

    /**
     * 更新资源状态
     *
     * @param idList 资源ID列表
     * @param enable 资源状态
     * @return 结果
     */
    Collection<? extends User> updateEanble(Collection<String> idList, boolean enable);

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
     * @return 用户信息
     */
    User deleteUserRole(String userId, String roleId);

    /**
     * 添加用户到用户组
     *
     * @param userId 用户ID
     * @param groupId 用户组ID
     * @return 用户信息
     */
    User addGroupUser(String userId, String groupId);

    /**
     * 用户组中移除用户
     *
     * @param userId 用户ID
     * @param groupId 用户组ID
     * @return 用户信息
     */
    User deleteGroupUser(String userId, String groupId);

}
