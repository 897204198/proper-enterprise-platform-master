package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Override
    public Role get(String id) {
        return roleDao.findOne(id);
    }


    @Override
    public Role save(Role role) {
        setDefault(role);
        validateRoleName(role);
        validateRoleAndParentCircle(role);
        String parentId = role.getParentId();
        role = roleDao.save(role);
        if (StringUtil.isNotEmpty(parentId)) {
            role = addParent(role.getId(), parentId);
        }
        return role;
    }


    @Override
    public boolean delete(Role role) {
        if (role == null || StringUtil.isBlank(role.getId())) {
            return false;
        }
        return this.deleteByIds(role.getId());
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            Collection<? extends Role> list = roleDao.findAll(idList);
            for (Role roleEntity : list) {
                validateBeforeDelete(roleEntity);
            }
            Collection<? extends Role> childRoles = roleDao.findRolesByParentId(idList);
            for (Role childRole : childRoles) {
                childRole.addParent(null);
                roleDao.save(childRole);
            }
            roleDao.delete(list);
            ret = true;
        }
        return ret;
    }

    @Override
    public Role update(Role role) {
        //todo  update不应处理关系  为了不让接口变动 暂时支持 等联调后统一修改接口
        validateRoleName(role);
        validateRoleAndParentCircle(role);
        String parentId = role.getParentId();
        Role updateRole = roleDao.updateForSelective(role);
        if (StringUtil.isNotEmpty(parentId)) {
            addParent(role.getId(), parentId);
        }
        return updateRole;
    }

    public Role addParent(String roleId, String parentId) {
        Role role = roleDao.findOne(roleId);
        if (null == role) {
            return null;
        }
        if (StringUtil.isEmpty(parentId)) {
            return role;
        }
        Role parentRole = roleDao.findOne(parentId);
        if (null == parentRole) {
            return role;
        }
        role.addParent(parentRole);
        return roleDao.save(role);
    }


    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<? extends Role> findRolesPagniation(String name, String description, String parentId, EnableEnum enable) {
        return roleDao.findRolesPagniation(name, description, parentId, enable);
    }


    @Override
    public Collection<? extends Role> findRoles(String name, EnableEnum enable) {
        return roleDao.findRoles(name, enable);
    }

    @Override
    public Collection<? extends Role> findRolesLike(String name, EnableEnum enable) {
        return roleDao.findRolesLike(name, enable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Role> findRolesLike(String name, String description, String parentId, EnableEnum enable) {
        return roleDao.findRolesLike(name, description, parentId, enable);
    }

    @Override
    public Collection<? extends Role> findRoleParents(String roleId) {
        Role role = roleDao.findOne(roleId);
        if (null == role) {
            return new ArrayList<>();
        }
        return recursionRoleParents(role, new HashSet());
    }


    @Override
    public Collection<? extends Role> getFilterRoles(Collection<? extends Role> roles) {
        return getFilterRoles(roles, EnableEnum.ENABLE);
    }

    @Override
    public Collection<? extends Role> getFilterRoles(Collection<? extends Role> roles, EnableEnum roleEnable) {
        Collection<Role> result = new HashSet<>();
        for (Role role : roles) {
            if (EnableEnum.ALL == roleEnable) {
                result.add(role);
                continue;
            }
            if (EnableEnum.ENABLE == roleEnable && role.getEnable()) {
                result.add(role);
                continue;
            }
            if (EnableEnum.DISABLE == roleEnable && !role.getEnable()) {
                result.add(role);
            }
        }
        return result;
    }

    @Override
    public Collection<? extends Role> updateEnable(Collection<String> idList, boolean enable) {
        if (CollectionUtil.isEmpty(idList)) {
            return new ArrayList<>();
        }
        Collection<? extends Role> roles = roleDao.findAll(idList);
        for (Role role : roles) {
            role.setEnable(enable);
        }
        return roleDao.save(roles);
    }

    @Override
    public Collection<? extends Menu> getRoleMenus(String roleId, EnableEnum menuEnable) {
        Collection<Menu> result = new HashSet<>();
        Role role = this.get(roleId);
        if (role == null) {
            return new ArrayList<>();
        }
        Collection currentMenus = menuService.getFilterMenusAndParent(role.getMenus());
        result.addAll(currentMenus);
        //获取父角色集合
        Collection<? extends Role> parentList = this.findParentRoles(roleId);
        for (Role detail : parentList) {
            currentMenus = menuService.getFilterMenusAndParent(this.getRoleMenus(detail.getId(), menuEnable));
            result.addAll(currentMenus);
        }
        return result;
    }

    @Override
    public Role addRoleMenus(String roleId, List<String> ids) {
        Role role = this.get(roleId);
        if (role == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.role.get.failed"));
        }
        if (CollectionUtil.isEmpty(ids)) {
            return role;
        }
        Collection<Menu> menuList = new ArrayList<>();
        for (String id : ids) {
            if (haveMenu(id, role)) {
                continue;
            }
            menuList.add(menuService.get(id));
        }
        role.add(menuList);
        role = this.save(role);
        return role;
    }


    @Override
    public Role deleteRoleMenus(String roleId, String ids) {
        Role role = this.get(roleId);
        if (role == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.role.get.failed"));
        }
        if (StringUtil.isNotNull(ids)) {
            Collection<Menu> menuList = new ArrayList<>();
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                menuList.add(menuService.get(id));
            }
            role.remove(menuList);
            role = this.save(role);
        }
        return role;
    }

    private boolean haveMenu(String menuId, Role role) {
        if (null == role || CollectionUtil.isEmpty(role.getMenus())) {
            return false;
        }
        for (Menu detail : role.getMenus()) {
            if (detail.getId().equals(menuId)) {
                return true;
            }
        }
        return false;
    }

    private Collection<? extends Role> recursionRoleParents(Role role, Collection<Role> roles) {
        if (null != role.getParent()) {
            roles.add(role.getParent());
            recursionRoleParents(role.getParent(), roles);
        }
        return roles;
    }

    @Override
    public Collection<? extends Resource> getRoleResources(String roleId, EnableEnum resourceEnable) {
        Role role = this.get(roleId);
        return getRoleResources(role, resourceEnable);
    }

    @Override
    public Collection<? extends Resource> getRoleResources(Collection<? extends Role> roles, EnableEnum resourceEnable) {
        Set resourceSet = new HashSet();
        for (Role role : roles) {
            resourceSet.addAll(getRoleResources(role, resourceEnable));
        }
        return resourceSet;
    }

    private Collection<? extends Resource> getRoleResources(Role role, EnableEnum resourceEnable) {
        if (role == null) {
            return new ArrayList<>();
        }
        return resourceService.getFilterResources(recursionResource(role, new HashSet(), false), resourceEnable);
    }

    private Collection<? extends Resource> recursionResource(Role role, Set resourceSet, boolean extend) {
        if (null == resourceSet) {
            resourceSet = new HashSet();
        }
        for (Resource resource : role.getResources()) {
            resource.setExtend(extend);
        }
        resourceSet.addAll(role.getResources());
        //若存在父角色  且父角色启用  则递归继续获得资源
        if (null != role.getParent() && role.getParent().getEnable()) {
            recursionResource(role.getParent(), resourceSet, true);
        }
        return resourceSet;
    }

    @Override
    public Collection<? extends Role> findParentRoles(String currentRoleId) {
        return roleDao.findParentRoles(currentRoleId);
    }

    @Override
    public Role addRoleResources(String roleId, List<String> ids) {
        Role role = roleDao.findOne(roleId);
        if (role == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.role.get.failed"));
        }
        if (ids != null) {
            Collection<Resource> resourceList = new ArrayList<>();
            for (String id : ids) {
                resourceList.add(resourceService.get(id));
            }
            role.addResources(resourceList);
            role = roleDao.save(role);
        }
        return role;
    }

    @Override
    public Role deleteRoleResources(String roleId, String ids) {
        Role role = roleDao.findOne(roleId);
        if (role == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.role.get.failed"));
        }
        if (StringUtil.isNotNull(ids)) {
            Collection<Resource> resourceList = new ArrayList<>();
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                resourceList.add(resourceService.get(id));
            }
            role.removeResources(resourceList);
            role = roleDao.save(role);
        }
        return role;
    }

    @Override
    public Collection<? extends User> getRoleUsers(String roleId, EnableEnum roleEnable, EnableEnum userEnable) {
        Role role = roleDao.findOne(roleId);
        if (role == null) {
            return new ArrayList<>();
        }
        return userService.getFilterUsers(role.getUsers(), userEnable);
    }

    @Override
    public Collection<? extends UserGroup> getRoleUserGroups(String roleId, EnableEnum roleEnable, EnableEnum userGroupEnable) {
        Role role = roleDao.findOne(roleId);
        if (role == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.role.get.failed"));
        }
        Collection<? extends UserGroup> userGroups = role.getUserGroups();
        return userGroups;
    }

    @Override
    public Map<String, Object> getUserGroupRolesByUserId(String userId) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.get(userId);
        Collection groups = user.getUserGroups();
        Iterator iterator = groups.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            UserGroup userGroupEntity = (UserGroup) object;
            Collection<? extends Role> roles = userGroupEntity.getRoles();
            for (Role role : roles) {
                if (!result.containsKey(role.getId())) {
                    result.put(role.getId(), role);
                }
            }
        }
        return result;
    }

    private void validateBeforeDelete(Role role) {
        if (CollectionUtil.isNotEmpty(role.getUsers())
            || CollectionUtil.isNotEmpty(role.getUserGroups())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.role.delete.relation.failed"));
        }
    }


    private void setDefault(Role role) {
        if (null == role.getEnable()) {
            role.setEnable(true);
        }
    }

    private void validateRoleName(Role role) {
        if (StringUtil.isEmpty(role.getName())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.role.name.empty"));
        }
        Collection<? extends Role> oldRoles = roleDao.findRoles(role.getName(), EnableEnum.ALL);
        for (Role oldRole : oldRoles) {
            if (null != oldRole && !oldRole.getId().equals(role.getId())) {
                throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.role.name.duplicate"));
            }
        }
    }

    private void validateRoleAndParentCircle(Role role) {
        if (role == null || StringUtil.isBlank(role.getId())) {
            return;
        }
        String parentId = role.getParentId();
        if (StringUtil.isEmpty(parentId)) {
            return;
        }
        role.addParent(roleDao.findOne(parentId));
        String currentRoleId = role.getId();
        while (true) {
            role = role.getParent();
            if (role == null || StringUtil.isBlank(role.getId())) {
                break;
            }
            if (role.getId().equals(currentRoleId)) {
                throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.role.circle.error"));
            }
        }
    }

}
