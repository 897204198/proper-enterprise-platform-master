package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.Map;

/**
 * 用户服务接口
 *
 * 用户名（username）作为用户唯一标识，可为用户所见
 * 用户ID（userId）同样为用户唯一标识，但对用户透明，仅在系统内部使用
 */
public interface UserService {

    User save(User user);

    void save(User... users);

    User save(String userId, Map<String, Object> user);

    /**
     * 保存或更新用户信息
     *
     * @param user 请求用户对象
     * @return 用户信息
     */
    User saveOrUpdateUser(User user);

    User getCurrentUser();

    User get(String id);

    User getByUsername(String username);

    User updateByUser(Map<String, Object> userMap);

    boolean delete(String id);

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
     * 通过用户名,显示名,手机号模糊查询用户列表
     *
     * @param condition 输入信息
     * @return 用户列表
     */
    Collection<? extends User> getUsersByCondition(String condition);

    /**
     * 按照查询条件获取用户信息列表
     *
     * @param userName 用户名
     * @param name     用户显示名
     * @param email    用户邮箱
     * @param phone    用户手机号
     * @param enable   用户可用/不可用
     * @return 用户信息列表
     */
    DataTrunk<? extends User> getUsersByCondition(String userName, String name, String email, String phone, String enable);

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
     * @return 用户信息(添加成功返回用户信息,失败返回null)
     */
    User deleteUserRole(String userId, String roleId);

    /**
     * 获取指定用户的用户组集合
     *
     * @param userId 用户ID
     * @return 用户组集合
     */
    Collection<? extends UserGroup> getUserGroups(String userId);

    /**
     * 获取指定用户角色集合
     *
     * @param userId 用户ID
     * @return 角色集合
     */
    Collection<? extends Role> getUserRoles(String userId);

    /**
     * 检测用户组是否含有此用户
     * @param userGroup 待检测用户组
     * @param userId 待检测用户ID
     * @return 有则返回那个user对象，没有则返回null
     */
    User groupHasTheUser(UserGroup userGroup, String userId);

    /**
     * 根据传入的用户集合，获取合法的用户集合(过滤掉valid、enable为false的)
     * @param users 待检测的用户集合
     * @return 返回合法的用户集合
     */
    Collection<? extends User> getFilterUsers(Collection<? extends User> users);

    /**
     * 根据请求的url、请求的方法，判断当前用户是否有此权限
     * 用户组也有资源，某个人的用户组有此资源，也视为这个人有此资源
     * @param user 待检测用户
     * @param reqUrl 请求的url
     * @param requestMethod 请求的方法
     * @return 有权限返回真，没有返回假
     */
    boolean hasPermissionOfUser(User user, String reqUrl, RequestMethod requestMethod);

    /**
     * 检测当前登录用户是否有对应的权限
     * @param requestMethod 请求方法
     * @param reqUrl 请求路径
     */
    void checkPermission(String reqUrl, RequestMethod requestMethod);

}
