package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;
import java.util.List;

public interface RoleDao extends BaseService<Role, String> {

    /**
     * 获取新的角色
     * @return 新的角色实体类
     */
    Role getNewRole();

    /**
     * 新增角色
     * @param role 角色
     * @return 角色
     */
    Role save(Role role);

    /**
     * 修改角色
     * @param role 角色
     * @return 角色
     */
    Role updateForSelective(Role role);

    /**
     * 通过id查询
     * @param idList 角色id集合
     * @return 角色集合
     */
    Collection<? extends Role> findRoles(Collection<String> idList);

    /**
     * 获取角色
     * @param enable 是否可用
     * @return 角色集合
     */
    Collection<? extends Role> findRoles(EnableEnum enable);

    /**
     * 通过name 获取角色
     * @param name 角色名称
     * @param enable 是否可用
     * @return  角色集合
     */
    Collection<? extends Role> findRoles(String name, EnableEnum enable);

    /**
     * 通过id获取角色
     * @param parentIds 上级角色
     * @return 角色集合
     */
    Collection<? extends Role> findRolesByParentId(List<String> parentIds);

    /**
     * 通过name模糊查询
     * @param name 角色名称
     * @param enable 是否可用
     * @return 角色集合
     */
    Collection<? extends Role> findRolesLike(String name, EnableEnum enable);

    /**
     * 模糊查询
     * @param name 角色名称
     * @param description 角色描述信息
     * @param parentId 上级角色
     * @param enable 是否可用
     * @return 角色集合
     */
    Collection<? extends Role> findRolesLike(String name, String description, String parentId, EnableEnum enable);

    /**
     * 模糊查询带分页
     * @param name 角色名称
     * @param description 角色描述
     * @param parentId 上级角色
     * @param enable 是否可用
     * @return 角色集合
     */
    DataTrunk<? extends Role> findRolesPagination(String name, String description, String parentId, EnableEnum enable);

    /**
     * 通过roleid获取上级角色
     * @param currentRoleId  当前的角色id
     * @return  上级角色集合
     */
    Collection<? extends Role> findParentRoles(String currentRoleId);

}
