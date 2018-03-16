package com.proper.enterprise.platform.auth.common.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity;
import com.proper.enterprise.platform.auth.common.jpa.repository.RoleRepository;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.JpaServiceSupport;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class RoleDaoImpl extends JpaServiceSupport<Role, RoleRepository, String> implements RoleDao {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleRepository getRepository() {
        return roleRepository;
    }

    @Autowired
    private I18NService i18NService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserGroupService userGroupService;

    @Override
    public Role get(String id) {
        return roleRepository.findOne(id);
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
    public Role getNewRole() {
        return new RoleEntity();
    }

    @Override
    public Role findByIdAndValid(String id, boolean valid) {
        return roleRepository.findByIdAndValid(id, valid);
    }

    @Override
    public Collection<? extends Role> findAllByNameLike(String name) {
        return roleRepository.findAllByNameLike(name);
    }

    @Override
    public Collection<? extends Role> findAll(Collection<String> idList) {
        return roleRepository.findAll(idList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Role> getByCondition(String name, String description, String parentId, String enable) {
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
                    predicates.add(cb.equal(root.get("parent").get("id"), parentId));
                }
                if (StringUtil.isNotNull(enable)) {
                    predicates.add(cb.equal(root.get("enable"), enable.equals("Y")));
                }
                predicates.add(cb.equal(root.get("valid"), true));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Collection<? extends Role> roles = roleRepository.findAll(specification, new Sort("name"));
        for (Role roleEntity : roles) {
            if (roleEntity.getParent() != null) {
                roleEntity.setParentId(roleEntity.getParent().getId());
            }
        }
        return roles;
    }

    @Override
    public Collection<? extends Role> findAllByValidTrueAndEnableTrue() {
        return roleRepository.findAllByValidTrueAndEnableTrue();
    }

    @Override
    public Collection<? extends Role> getParentRolesByCurrentRoleId(String currentRoleId) {
        Collection<Role> result = new LinkedList<>();
        Role role = this.get(currentRoleId);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        Role parent = role.getParent();
        while (true) {
            if (parent == null || !parent.isValid() || !parent.isEnable()) {
                break;
            }
            if (currentRoleId.equals(parent.getId())) {
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.circle.error"));
            }
            result.add(parent);
            parent = parent.getParent();
        }
        return result;
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
    public Collection<? extends UserGroup> getRoleUserGroups(String roleId) {
        Collection<? extends User> users = roleService.getRoleUsers(roleId);
        Iterator iterator = users.iterator();
        Collection<? extends UserGroup> filterGroups = new HashSet<>();
        Collection groups;
        while (iterator.hasNext()) {
            User user = (User) iterator.next();
            groups = userGroupService.getFilterGroups(user.getUserGroups());
            filterGroups.addAll(groups);
        }
        return filterGroups;
    }


}
