package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.entity.DataTrunk;

import java.util.Collection;
import java.util.List;

public interface UserGroupService {

    /**
     * 用户组集合
     *
     * @param name        用户组名称
     * @param description 用户组描述
     * @param enable      用户组状态
     * @return 用户组集合
     */
    Collection<? extends UserGroup> getGroups(String name, String description, EnableEnum enable);

    /**
     * 用户组集合
     *
     * @param name        用户组名称
     * @param description 用户组描述
     * @param enable      用户组状态
     * @return 用户组集合 分页
     */
    DataTrunk<? extends UserGroup> getGroupsPagniation(String name, String description, EnableEnum enable);

    /**
     * 根据传入的用户组集合，获取合法的用户组(过滤掉valid、enable为false的用户组)
     *
     * @param groups 待过滤的用户组集合
     * @return 返回合法的用户组集合
     */
    Collection<? extends UserGroup> getFilterGroups(Collection<? extends UserGroup> groups);

    /**
     * 根据传入的用户组集合，获取合法的用户组(过滤掉valid、enable为false的用户组)
     *
     * @param groups          待过滤的用户组集合
     * @param userGroupEnable 用户组是否禁用
     * @return 返回合法的用户组集合
     */
    Collection<? extends UserGroup> getFilterGroups(Collection<? extends UserGroup> groups, EnableEnum userGroupEnable);

    /**
     * 根据 ID 获得用户组
     *
     * @param id 用户组 ID
     * @return 用户组
     */
    UserGroup get(String id);

    /**
     * 根据 ID 获得用户组
     *
     * @param id     用户组 ID
     * @param enable 启用禁用
     * @return 用户组
     */
    UserGroup get(String id, EnableEnum enable);

    /**
     * 保存或更新用户组信息
     *
     * @param group 用户组
     * @return 更新后的用户组
     */
    UserGroup save(UserGroup group);

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
    Collection<? extends UserGroup> updateEnable(Collection<String> idList, boolean enable);

    /**
     * 用户组添加角色
     *
     * @param groupId 用户组ID
     * @param roleId  角色ID
     * @return 用户组
     */
    UserGroup saveUserGroupRole(String groupId, String roleId);

    /**
     * 保存或更新用户组
     *
     * @param userGroup 请求用户组对象
     * @return 保存或更新后的用户组信息信息
     */
    UserGroup saveOrUpdateUserGroup(UserGroup userGroup);

    /**
     * 获取指定用户组角色集合
     *
     * @param groupId 用户组ID
     * @return 角色集合
     */
    Collection<? extends Role> getGroupRoles(String groupId, EnableEnum userGroupEnable, EnableEnum roleEnable);

    /**
     * 用户组删除角色
     *
     * @param groupId 用户组ID
     * @param roleId  角色ID
     * @return 用户组
     */
    UserGroup deleteUserGroupRole(String groupId, String roleId);

    /**
     * 用户组添加用户
     *
     * @param groupId 用户组ID
     * @param userId  用户ID
     * @return 用户组
     */
    UserGroup addGroupUser(String groupId, String userId);

    /**
     * 用户组删除用户
     *
     * @param groupId 用户组ID
     * @param userId  用户ID
     * @return 用户组
     */
    UserGroup deleteGroupUser(String groupId, String userId);

    /**
     * 从用户组中删除多条用户
     *
     * @param groupId 用户组ID
     * @param ids  用户 ids
     * @return 用户组
     */
    UserGroup deleteGroupUsers(String groupId, String ids);

    /**
     * 用户组批量添加用户
     *
     * @param groupId 用户组ID
     * @param ids 用户ID集合
     * @return 用户组
     */
    UserGroup addGroupUserByUserIds(String groupId, List<String> ids);

    /**
     * 获取指定用户组的用户集合
     *
     * @param groupId 用户组ID
     * @return 用户集合
     */
    Collection<? extends User> getGroupUsers(String groupId, EnableEnum userGroupEnable, EnableEnum userEnable);

    /**
     * 创建更新用户组信息
     *
     * @param userGroup 新增用户组信息
     * @return 更新后的用户组
     */
    UserGroup createUserGroup(UserGroup userGroup);

}
