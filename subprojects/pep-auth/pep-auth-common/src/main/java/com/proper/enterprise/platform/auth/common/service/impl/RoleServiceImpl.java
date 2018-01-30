package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.api.auth.service.*;
import com.proper.enterprise.platform.auth.common.entity.*;
import com.proper.enterprise.platform.auth.common.repository.RoleRepository;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private I18NService i18NService;

    @Override
    public Role get(String id) {
        Role role = roleRepository.findOne(id);
        if (role != null && role.isValid() && role.isEnable()) {
            return role;
        }
        return null;
    }

    @Override
    public Collection<? extends Role> getByName(String name) {
        return roleRepository.findByNameAndValidAndEnable(name, true, true);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save((RoleEntity) role);
    }

    @Override
    public Role save(Map<String, Object> map) {
        String id = String.valueOf(map.get("id"));
        RoleEntity role = new RoleEntity();
        // 更新
        if (map.get("id") != null && StringUtil.isNotNull(id)) {
            role = (RoleEntity) this.get(id);
        }
        boolean enable = map.get("enable") == null ? role.isEnable() : (boolean) map.get("enable");
        if (!enable) {
            Collection childrenRols = this.getByCondiction("", "", role.getId(), "Y");
            if (!childrenRols.isEmpty()) {
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.delete.relation.failed"));
            }
        }
        role.setDescription(String.valueOf(map.get("description")));
        role.setName(String.valueOf(map.get("name")));
        role.setEnable(enable);
        String parentId = String.valueOf(map.get("parentId"));
        if (map.get("parentId") != null && StringUtil.isNotNull(parentId)) {
            role.setParent(this.get(parentId));
            if (this.hasCircleInheritForCurrentRole(role)) {
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.circle.error"));
            }
        }
        return roleRepository.save(role);
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
    public Collection<? extends Role> getByCondiction(String name, String description, String parentId, String enable) {
        Specification specification = new Specification<RoleEntity>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtil.isNotNull(name)) {
                    predicates.add(cb.like(root.get("name"), "%".concat(name).concat("%")));
                }
                if (StringUtil.isNotNull(description)) {
                    predicates.add(cb.like(root.get("description"), "%".concat(description).concat("%")));
                }
                if (StringUtil.isNotNull(parentId)) {
                    predicates.add(cb.like(root.get("parent").get("id"), "%".concat(parentId).concat("%")));
                }
                if (StringUtil.isNotNull(enable)) {
                    predicates.add(cb.equal(root.get("enable"), enable.equals("Y")));
                }
                predicates.add(cb.equal(root.get("valid"), true));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return roleRepository.findAll(specification, new Sort("name"));
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            List<RoleEntity> list = roleRepository.findAll(idList);
            Collection childrenRols;
            for (RoleEntity roleEntity : list) {
                if (roleEntity == null || !roleEntity.isEnable() || !roleEntity.isValid()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
                }
                if (!roleEntity.getResources().isEmpty() || !roleEntity.getUsers().isEmpty() || !roleEntity.getMenus().isEmpty()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.delete.relation.failed"));
                }

                //如果有别的角色继承当前角色，也不能删除
                childrenRols = this.getByCondiction("", "", roleEntity.getId(), "Y");
                if (!childrenRols.isEmpty()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.delete.relation.failed"));
                }

                roleEntity.setValid(false);
                roleEntity.setEnable(false);
                roleEntity.setParent(null);
            }
            roleRepository.save(list);
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends Role> getRoleParents() {
        List<RoleEntity> list = roleRepository.findAllByValidTrueAndEnableTrue();
        Set<String> roleId = new HashSet<>();
        List<RoleEntity> result = new ArrayList<>();
        for (RoleEntity roleEntity : list) {
            Role parentRole = roleEntity.getParent();
            if (parentRole != null && !roleId.contains(parentRole.getId())) {
                roleId.add(parentRole.getId());
                result.add((RoleEntity) parentRole);
            }
        }
        return result;
    }

    @Override
    public Collection<? extends Role> getFilterRoles(Collection<? extends Role> roles) {
        Collection<RoleEntity> result = new HashSet<>();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            RoleEntity role = (RoleEntity) iterator.next();
            if (!role.isEnable() || !role.isValid()) {
                continue;
            }
            result.add(role);
        }
        return result;
    }

    @Override
    public Collection<? extends Role> updateEanble(Collection<String> idList, boolean enable) {
        List<RoleEntity> roleList = new ArrayList<>();
        Collection childrenRols;
        for (String id : idList) {
            childrenRols = this.getByCondiction("", "", id, "Y");
            if (!enable && !childrenRols.isEmpty()) {
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.delete.relation.failed"));
            }
            RoleEntity roleEntity = roleRepository.findByIdAndValid(id, true);
            roleEntity.setEnable(enable);
            roleList.add(roleEntity);
        }
        return roleRepository.save(roleList);
    }

    @Override
    public Collection<? extends Menu> getRoleMenus(String roleId) {
        Collection<MenuEntity> result = new HashSet<>();
        Role role = this.get(roleId);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        Collection currentMenus = menuService.getFilterMenusAndParent(role.getMenus());
        result.addAll(currentMenus);
        //获取父角色集合
        List<RoleEntity> parentList = this.getParentRolesByCurrentRoleId(roleId);
        for (RoleEntity roleEntity : parentList) {
            currentMenus = menuService.getFilterMenusAndParent(roleEntity.getMenus());
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
            Collection<MenuEntity> menuList = new ArrayList<>();
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                menuList.add((MenuEntity) menuService.get(id));
            }
            role.add(menuList);
            role = save(role);
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
            Collection<MenuEntity> menuList = new ArrayList<>();
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                menuList.add((MenuEntity) menuService.get(id));
            }
            role.remove(menuList);
            role = save(role);
        }
        return role;
    }

    @Override
    public Collection<? extends Resource> getRoleResources(String roleId) {
        Role role = this.get(roleId);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        return resourceService.getFilterResources(role.getResources());
    }

    @Override
    public List<RoleEntity> getParentRolesByCurrentRoleId(String currentRoleId) {
        List<RoleEntity> result = new LinkedList<>();
        RoleEntity roleEntity = (RoleEntity) this.get(currentRoleId);
        if (roleEntity == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        RoleEntity parent = (RoleEntity) roleEntity.getParent();
        while (true) {
            if (parent == null || !parent.isValid() || !parent.isEnable()) {
                break;
            }
            if (currentRoleId.equals(parent.getId())) {
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.circle.error"));
            }
            result.add(parent);
            parent = (RoleEntity) parent.getParent();
        }
        return result;
    }

    @Override
    public Role addRoleResources(String roleId, String ids) {
        Role role = this.get(roleId);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            Collection<ResourceEntity> resourceList = new ArrayList<>();
            for (String id : idArr) {
                resourceList.add((ResourceEntity) resourceService.get(id));
            }
            role.addResources(resourceList);
            role = save(role);
        }
        return role;
    }

    @Override
    public Role deleteRoleResources(String roleId, String ids) {
        Role role = this.get(roleId);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        if (StringUtil.isNotNull(ids)) {
            Collection<ResourceEntity> resourceList = new ArrayList<>();
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                resourceList.add((ResourceEntity) resourceService.get(id));
            }
            role.removeResources(resourceList);
            role = save(role);
        }
        return role;
    }

    @Override
    public Collection<? extends User> getRoleUsers(String roleId) {
        Role role = this.get(roleId);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        return userService.getFilterUsers(role.getUsers());
    }

    @Override
    public Collection<? extends UserGroup> getRoleUserGroups(String roleId) {
        Collection users = this.getRoleUsers(roleId);
        Iterator iterator = users.iterator();
        Collection<? extends UserGroup> filterGroups = new HashSet<>();
        Collection groups;
        while (iterator.hasNext()) {
            UserEntity userEntity = (UserEntity) iterator.next();
            groups = userGroupService.getFilterGroups(userEntity.getUserGroups());
            filterGroups.addAll(groups);
        }
        return filterGroups;
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
                RoleEntity roleEntity = (RoleEntity) iterator.next();
                if (roleId.equals(roleEntity.getId())) {
                    return roleEntity;
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
            UserGroupEntity userGroupEntity = (UserGroupEntity) object;
            Collection<RoleEntity> roles = (Collection<RoleEntity>) userGroupEntity.getRoles();
            for (RoleEntity roleEntity : roles) {
                if (!result.containsKey(roleEntity.getId())) {
                    result.put(roleEntity.getId(), roleEntity);
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
        boolean result = false;
        if (currentRole == null || StringUtil.isBlank(currentRole.getId())) {
            return result;
        }
        String currentRoleId = currentRole.getId();
        while (true) {
            currentRole = currentRole.getParent();
            if (currentRole == null || StringUtil.isBlank(currentRole.getId())) {
                break;
            }
            if (currentRole.getId().equals(currentRoleId)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public boolean hasPermissionOfRole(Role role, String reqUrl, RequestMethod requestMethod) {
        if (role == null || !role.isValid() || !role.isEnable()) {
            return false;
        }
        Iterator iterator1 = role.getResources().iterator();
        while (iterator1.hasNext()) {
            Resource resource = (Resource) iterator1.next();
            if (resourceService.hasPermissionOfResource(resource, reqUrl, requestMethod)) {
                return true;
            }
        }
        return false;
    }
}
