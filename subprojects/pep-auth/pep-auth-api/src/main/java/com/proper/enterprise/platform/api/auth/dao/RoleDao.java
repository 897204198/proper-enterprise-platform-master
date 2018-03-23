package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

public interface RoleDao extends BaseService<Role, String> {

    Role getNewRole();

    Role get(String id);

    Role get(String id, EnableEnum enableEnum);

    Collection<? extends Role> getByName(String name);

    Role save(Role role);

    Role findByIdAndValid(String id, boolean valid);

    Collection<? extends Role> findAllByNameLike(String name);

    Collection<? extends Role> findAll(Collection<String> idList);

    Collection<? extends Role> getByCondition(String name, String description, String parentId, String enable);

    Collection<? extends Role> findAllByValidTrueAndEnableTrue();

    /**
     * 获取当前节点的父节点集合
     */
    Collection<? extends Role> getParentRolesByCurrentRoleId(String currentRoleId);

    /**
     * 检测是否有经过当前角色的循环继承
     *
     * @param currentRole 当前用户
     * @return 有则返回真，没有返回假
     */
    boolean hasCircleInheritForCurrentRole(Role currentRole);

    Collection<? extends UserGroup> getRoleUserGroups(String roleId);

}
