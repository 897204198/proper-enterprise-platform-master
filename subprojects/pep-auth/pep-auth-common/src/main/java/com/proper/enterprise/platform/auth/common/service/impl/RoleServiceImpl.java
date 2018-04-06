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
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
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

    @Autowired
    private I18NService i18NService;

    @Override
    public Role get(String id) {
        Role role = roleDao.get(id);
        if (role != null && role.isValid() && role.isEnable()) {
            return role;
        }
        return null;
    }

    @Override
    public Role get(String id, EnableEnum enableEnum) {
        return roleDao.get(id, enableEnum);
    }

    @Override
    public Collection<? extends Role> getByName(String name) {
        return roleDao.getByName(name);
    }

    @Override
    public Collection<? extends Role> getAllSimilarRolesByName(String nameLike) {
        return roleDao.findAllByNameLike(nameLike);
    }

    @Override
    public Role save(Role role) {
        return roleDao.save(role);
    }

    @Override
    public Role saveOrUpdateRole(Role roleReq) {
        String id = roleReq.getId();
        Role role = roleDao.getNewRole();
        // 更新
        if (StringUtil.isNotNull(id)) {
            role = this.get(id);
        }
        boolean enable = roleReq.isEnable();
        if (!enable) {
            Collection childrenRols = this.getByCondition("", "", role.getId(), EnableEnum.ENABLE);
            if (!childrenRols.isEmpty()) {
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.delete.relation.failed"));
            }
        }
        role.setDescription(roleReq.getDescription());
        role.setName(roleReq.getName());
        role.setEnable(enable);
        String parentId = roleReq.getParentId();
        if (StringUtil.isNotNull(parentId)) {
            role.setParent(roleDao.get(parentId, EnableEnum.ENABLE));
            if (this.hasCircleInheritForCurrentRole(role)) {
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.circle.error"));
            }
        }
        return roleDao.save(role);
    }

    @Override
    public void delete(Role role) {
        if (role == null || StringUtil.isBlank(role.getId())) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        this.deleteByIds(role.getId());
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<? extends Role> findRolesPagniation(String name, String description, String parentId, EnableEnum enable) {
        return roleDao.findRolesPagniation(name, description, parentId, enable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Role> getByCondition(String name, String description, String parentId, EnableEnum enable) {
        return roleDao.getByCondition(name, description, parentId, enable);
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            Collection<? extends Role> list = roleDao.findAll(idList);
            Collection childrenRols;
            for (Role roleEntity : list) {
                if (roleEntity == null || !roleEntity.isEnable() || !roleEntity.isValid()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
                }
                if (!roleEntity.getResources().isEmpty() || !roleEntity.getUsers().isEmpty() || !roleEntity.getMenus().isEmpty()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.delete.relation.failed"));
                }

                //如果有别的角色继承当前角色，也不能删除
                childrenRols = this.getByCondition("", "", roleEntity.getId(), EnableEnum.ENABLE);
                if (!childrenRols.isEmpty()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.delete.relation.failed"));
                }

                roleEntity.setValid(false);
                roleEntity.setEnable(false);
                roleEntity.setParent(null);
            }
            roleDao.save(list);
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends Role> getRoleParents(String roleId) {
        Collection<? extends Role> list = roleDao.findAllByValidTrueAndEnableTrue();
        Collection<Role> result = new ArrayList<>();
        for (Role roleEntity : list) {
            Role parentRole = roleEntity.getParent();
            if (parentRole != null && !roleId.contains(parentRole.getId())) {
                roleId = parentRole.getId();
                result.add(parentRole);
            }
        }
        return result;
    }

    @Override
    public Collection<? extends Role> getFilterRoles(Collection<? extends Role> roles) {
        return getFilterRoles(roles, EnableEnum.ENABLE);
    }

    @Override
    public Collection<? extends Role> getFilterRoles(Collection<? extends Role> roles, EnableEnum roleEnable) {
        Collection<Role> result = new HashSet<>();
        for (Role role : roles) {
            if (EnableEnum.ALL == roleEnable && role.isValid()) {
                result.add(role);
                continue;
            }
            if (EnableEnum.ENABLE == roleEnable && role.isEnable() && role.isValid()) {
                result.add(role);
                continue;
            }
            if (EnableEnum.DISABLE == roleEnable && !role.isEnable() && role.isValid()) {
                result.add(role);
            }
        }
        return result;
    }

    @Override
    public Collection<? extends Role> updateEnable(Collection<String> idList, boolean enable) {
        List<Role> roleList = new ArrayList<>();
        Collection childrenRols;
        for (String id : idList) {
            childrenRols = this.getByCondition("", "", id, EnableEnum.ENABLE);
            if (!enable && !childrenRols.isEmpty()) {
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.delete.relation.failed"));
            }
            Role roleEntity = roleDao.findByIdAndValid(id, true);
            roleEntity.setEnable(enable);
            roleList.add(roleEntity);
        }
        return roleDao.save(roleList);
    }

    @Override
    public Collection<? extends Menu> getRoleMenus(String roleId, EnableEnum roleEnable, EnableEnum menuEnable) {
        Collection<Menu> result = new HashSet<>();
        Role role = this.get(roleId, roleEnable);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        Collection currentMenus = menuService.getFilterMenusAndParent(role.getMenus());
        result.addAll(currentMenus);
        //获取父角色集合
        Collection<? extends Role> parentList = this.getParentRolesByCurrentRoleId(roleId);
        for (Role detail : parentList) {
            currentMenus = menuService.getFilterMenusAndParent(this.getRoleMenus(detail.getId(), roleEnable, menuEnable));
            result.addAll(currentMenus);
        }
        return result;
    }

    @Override
    public Role addRoleMenus(String roleId, String ids) {
        Role role = this.get(roleId);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        if (StringUtil.isNotNull(ids)) {
            Collection<Menu> menuList = new ArrayList<>();
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                menuList.add(menuService.get(id));
                for (Menu detail : role.getMenus()) {
                    if (detail.getId().equals(id)) {
                        throw new ErrMsgException("pep.auth.common.role.has.menu");
                    }
                }
            }
            role.add(menuList);
            role = this.save(role);
        }
        return role;
    }

    @Override
    public Role deleteRoleMenus(String roleId, String ids) {
        Role role = this.get(roleId);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
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

    @Override
    public Collection<? extends Resource> getRoleResources(String roleId, EnableEnum roleEnable, EnableEnum resourceEnable) {
        Role role = this.get(roleId, roleEnable);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        return resourceService.getFilterResources(role.getResources());
    }

    @Override
    public Collection<? extends Role> getParentRolesByCurrentRoleId(String currentRoleId) {
        return roleDao.getParentRolesByCurrentRoleId(currentRoleId);
    }

    @Override
    public Role addRoleResources(String roleId, String ids) {
        Role role = roleDao.get(roleId, EnableEnum.ENABLE);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            Collection<Resource> resourceList = new ArrayList<>();
            for (String id : idArr) {
                resourceList.add(resourceService.get(id));
            }
            role.addResources(resourceList);
            role = roleDao.save(role);
        }
        return role;
    }

    @Override
    public Role deleteRoleResources(String roleId, String ids) {
        Role role = roleDao.get(roleId);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
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
        Role role = roleDao.get(roleId, roleEnable);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        return userService.getFilterUsers(role.getUsers());
    }

    @Override
    public Collection<? extends UserGroup> getRoleUserGroups(String roleId, EnableEnum roleEnable, EnableEnum userGroupEnable) {
        return roleDao.getRoleUserGroups(roleId);
    }

    @Override
    public Role userHasTheRole(User user, String roleId) {
        Collection roles = null;
        if (user != null) {
            roles = user.getRoles();
        }
        if (roles != null && roles.size() > 0 && StringUtil.isNotBlank(roleId)) {
            Iterator iterator = roles.iterator();
            while (iterator.hasNext()) {
                Role role = (Role) iterator.next();
                if (roleId.equals(role.getId())) {
                    return role;
                }
            }
        }
        return null;
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

    @Override
    public boolean hasCircleInherit(List<? extends Role> roles) {
        for (Role currentRole : roles) {
            if (this.hasCircleInheritForCurrentRole(currentRole)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCircleInheritForCurrentRole(Role currentRole) {
        return roleDao.hasCircleInheritForCurrentRole(currentRole);
    }

}
