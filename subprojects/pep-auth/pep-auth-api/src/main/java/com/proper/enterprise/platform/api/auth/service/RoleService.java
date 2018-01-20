package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Role;

import java.util.Collection;
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
     * 根据角色名称获得角色集合
     *
     * 系统使用角色 ID 作为角色唯一标识，允许存在同名的角色
     *
     * @param name 角色名称
     * @return 角色集合
     */
    Collection<? extends Role> getByName(String name);

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
     * @param map 请求参数
     * @return 角色信息
     */
    Role save(Map<String, Object> map);

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
     * @param name 名称
     * @param description 描述
     * @param parentId 父ID
     * @param enable 是否可用
     * @return 权限列表
     */
    Collection<? extends Role> getByCondiction(String name, String description, String parentId, String enable);

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
    Collection<? extends Role> getRoleParents();

    /**
     * 更新角色状态
     *
     * @param idList 角色ID列表
     * @param enable 角色状态
     * @return 结果
     */
    Collection<? extends Role> updateEanble(Collection<String> idList, boolean enable);

    /**
     * 角色添加菜单列表
     *
     * @param roleId 角色ID
     * @param ids 以 , 分隔的菜单ID列表
     * @return 角色
     */
    Role addRoleMenus(String roleId, String ids);

    /**
     * 角色删除菜单列表
     *
     * @param roleId 角色ID
     * @param ids 以 , 分隔的菜单ID列表
     * @return 角色
     */
    Role deleteRoleMenus(String roleId, String ids);
}
