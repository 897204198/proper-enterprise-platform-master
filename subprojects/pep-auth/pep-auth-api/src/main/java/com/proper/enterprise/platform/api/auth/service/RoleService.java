package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 角色服务接口
 */
public interface RoleService {

    /**
     * 根据角色 ID 获得角色
     *
     * @param id 角色 ID
     * @return 角色
     */
    Role get(String id);

    /**
     * 根据角色 ID 获得角色
     *
     * @param id 角色 ID
     * @return 角色
     */
    Role get(String id, EnableEnum enableEnum);

    /**
     * 根据角色名称获得角色集合
     * <p>
     * 系统使用角色 ID 作为角色唯一标识，允许存在同名的角色
     *
     * @param name 角色名称
     * @return 角色集合
     */
    Collection<? extends Role> getByName(String name);

    /**
     * 根据角色名字获取相近的角色集合
     * 系统使用角色 ID 作为角色唯一标识，允许存在同名的角色
     *
     * @author sunshuai
     */
    Collection<? extends Role> getAllSimilarRolesByName(String nameLike);

    /**
     * 保存或更新角色信息
     *
     * @param role 角色
     * @return 更新后的角色信息
     */
    Role save(Role role);

    /**
     * 保存或更新权限信息
     *
     * @param role 请求角色对象
     * @return 角色信息
     */
    Role saveOrUpdateRole(Role role);

    /**
     * 删除角色
     * 删除前需判断角色引用状态
     * 已经使用的角色不能删除
     *
     * @param role 角色
     */
    void delete(Role role);

    /**
     * 按照查询条件搜索权限列表
     *
     * @param name        名称
     * @param description 描述
     * @param parentId    父ID
     * @param enable      是否可用
     * @return 权限列表
     */
    Collection<? extends Role> getByCondition(String name, String description, String parentId, String enable);

    /**
     * 删除多条角色数据
     *
     * @param ids 以 , 分隔的待删除角色ID列表
     */
    boolean deleteByIds(String ids);

    /**
     * 获取角色父节点列表
     *
     * @return 父节点列表
     */
    Collection<? extends Role> getRoleParents(String roleId);

    /**
     * 从传入的角色集合中，获取有效的角色(valid、enable都是true的)
     *
     * @param roles 待过滤的角色集合
     * @return 返回有效的角色集合
     */
    Collection<? extends Role> getFilterRoles(Collection<? extends Role> roles);

    /**
     * 从传入的角色集合中，获取有效的角色(valid、enable都是true的)
     *
     * @param roles      待过滤的角色集合
     * @param roleEnable 角色是否禁用
     * @return 返回有效的角色集合
     */
    Collection<? extends Role> getFilterRoles(Collection<? extends Role> roles, EnableEnum roleEnable);

    /**
     * 更新角色状态
     *
     * @param idList 角色ID列表
     * @param enable 角色状态
     * @return 结果
     */
    Collection<? extends Role> updateEnable(Collection<String> idList, boolean enable);

    /**
     * 获取指定角色菜单集合
     *
     * @param roleId 角色ID
     * @return 菜单集合
     */
    Collection<? extends Menu> getRoleMenus(String roleId);

    /**
     * 角色添加菜单列表
     *
     * @param roleId 角色ID
     * @param ids    以 , 分隔的菜单ID列表
     * @return 角色
     */
    Role addRoleMenus(String roleId, String ids);

    /**
     * 角色删除菜单列表
     *
     * @param roleId 角色ID
     * @param ids    以 , 分隔的菜单ID列表
     * @return 角色
     */
    Role deleteRoleMenus(String roleId, String ids);

    /**
     * 获取指定角色资源集合
     *
     * @param roleId 角色ID
     * @return 资源集合
     */
    Collection<? extends Resource> getRoleResources(String roleId);

    /**
     * 根据当前角色ID，获取它的父角色链表，继承关系从前往后排列
     *
     * @return 返回父角色链表，从前往后依次父角色、父角色的父角色。。。。
     */
    Collection<? extends Role> getParentRolesByCurrentRoleId(String currentRoleId);

    /**
     * 角色添加资源列表
     *
     * @param roleId 角色ID
     * @param ids    以 , 分隔的资源ID列表
     * @return 角色
     */
    Role addRoleResources(String roleId, String ids);

    /**
     * 角色删除资源列表
     *
     * @param roleId 角色ID
     * @param ids    以 , 分隔的资源ID列表
     * @return 角色
     */
    Role deleteRoleResources(String roleId, String ids);

    /**
     * 获取指定角色用户集合
     *
     * @param roleId 角色ID
     * @return 用户集合
     */
    Collection<? extends User> getRoleUsers(String roleId);

    /**
     * 获取指定角色用户组集合
     *
     * @param roleId 角色ID
     * @return 用户组集合
     */
    Collection<? extends UserGroup> getRoleUserGroups(String roleId);

    /**
     * 检测用户(user),是否有此角色
     *
     * @param user   待检测用户
     * @param roleId 待检测角色ID
     * @return 有则返回那个role对象, 没有则返回null
     */
    Role userHasTheRole(User user, String roleId);

    /**
     * 通过用户ID，获取对应的用户组，拥有的角色
     *
     * @return 返回map类型，key为角色ID，value为角色对象
     */
    Map<String, Object> getUserGroupRolesByUserId(String userId);


    /**
     * 检测是否有循环继承
     *
     * @param roles 待检测角色列表
     * @return 有则返回真，没有返回假
     */
    boolean hasCircleInherit(List<? extends Role> roles);

    /**
     * 检测是否有经过当前角色的循环继承
     *
     * @param currentRole 当前用户
     * @return 有则返回真，没有返回假
     */
    boolean hasCircleInheritForCurrentRole(Role currentRole);

    /**
     * 检测指定的角色，有没有指定的资源权限
     *
     * @param role          指定的角色
     * @param reqUrl        指定的资源路径
     * @param requestMethod 指定的资源请求方法
     * @return 如果有，则返回真
     */
    boolean hasPermissionOfRole(Role role, String reqUrl, RequestMethod requestMethod);


}
