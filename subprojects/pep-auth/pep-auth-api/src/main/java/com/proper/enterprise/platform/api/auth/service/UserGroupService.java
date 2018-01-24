package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;

import java.util.Collection;
import java.util.Map;

public interface UserGroupService {

    /**
     * 用户组集合
     *
     * @param name 用户组名称
     * @param description 用户组描述
     * @param enable 用户组状态
     * @return 用户组集合
     */
    Collection<? extends UserGroup> getGroups(String name, String description, String enable);

    /**
     * 根据 ID 获得用户组
     *
     * @param  id 用户组 ID
     * @return 用户组
     */
    UserGroup get(String id);

    /**
     * 保存或更新用户组信息
     *
     * @param group 用户组
     * @return 更新后的用户组
     */
    UserGroup save(UserGroup group);

    /**
     * 保存或更新用户组信息
     *
     * @param map 请求参数
     * @return 更新后的用户组
     */
    UserGroup save(Map<String, Object> map);

    /**
     * 删除用户组
     * 删除前需判断用户组引用状态
     * 已经被引用（使用）的用户组不能删除
     *
     * @param group 用户组
     */
    void delete(UserGroup group);

    /**
     * 删除多条用户组数据
     *
     * @param ids 以 , 分隔的待删除用户组ID列表
     */
    boolean deleteByIds(String ids);

    /**
     * 更新菜单状态
     *
     * @param idList 菜单ID列表
     * @param enable 菜单状态
     * @return 结果
     */
    Collection<? extends UserGroup> updateEanble(Collection<String> idList, boolean enable);

    /**
     * 用户组添加角色
     *
     * @param groupId 用户组ID
     * @param roleId 角色ID
     * @return 用户组
     */
    UserGroup saveUserGroupRole(String groupId, String roleId);

    /**
     * 用户组删除角色
     *
     * @param groupId 用户组ID
     * @param roleId 角色ID
     * @return 用户组
     */
    UserGroup deleteUserGroupRole(String groupId, String roleId);

    /**
     * 获取指定用户组角色集合
     *
     * @param groupId 用户组ID
     * @return 角色集合
     */
    Collection<? extends Role> getGroupRoles(String groupId);

    /**
     * 用户组添加用户
     *
     * @param groupId 用户组ID
     * @param userId 用户ID
     * @return 用户组
     */
    UserGroup addGroupUser(String groupId, String userId);

    /**
     * 用户组删除用户
     *
     * @param groupId 用户组ID
     * @param userId 用户ID
     * @return 用户组
     */
    UserGroup deleteGroupUser(String groupId, String userId);

    /**
     * 获取指定用户组的用户集合
     *
     * @param groupId 用户组ID
     * @return 用户集合
     */
    Collection<? extends User> getGroupUsers(String groupId);

}
