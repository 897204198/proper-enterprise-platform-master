package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;
import java.util.List;

public interface RoleDao extends BaseService<Role, String> {

    Role getNewRole();

    Role save(Role role);

    Role updateForSelective(Role role);

    Collection<? extends Role> findRoles(Collection<String> idList);

    Collection<? extends Role> findRoles(EnableEnum enable);

    Collection<? extends Role> findRoles(String name, EnableEnum enable);

    Collection<? extends Role> findRolesByParentId(List<String> parentIds);

    Collection<? extends Role> findRolesLike(String name, EnableEnum enable);

    Collection<? extends Role> findRolesLike(String name, String description, String parentId, EnableEnum enable);

    DataTrunk<? extends Role> findRolesPagniation(String name, String description, String parentId, EnableEnum enable);

    Collection<? extends Role> findParentRoles(String currentRoleId);

}
