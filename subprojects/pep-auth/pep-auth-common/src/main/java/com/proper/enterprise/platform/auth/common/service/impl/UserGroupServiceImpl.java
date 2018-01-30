package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.RoleEntity;
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity;
import com.proper.enterprise.platform.auth.common.repository.UserGroupRepository;
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
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupRepository repository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private I18NService i18NService;

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends UserGroup> getGroups(String name, String description, String enable) {
        Specification specification = new Specification<UserGroupEntity>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtil.isNotNull(name)) {
                    predicates.add(cb.like(root.get("name"), "%".concat(name).concat("%")));
                }
                if (StringUtil.isNotNull(description)) {
                    predicates.add(cb.like(root.get("description"), "%".concat(description).concat("%")));
                }
                if (StringUtil.isNotNull(enable)) {
                    predicates.add(cb.equal(root.get("enable"), enable.equals("Y")));
                }
                predicates.add(cb.equal(root.get("valid"), true));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return repository.findAll(specification, new Sort("seq"));
    }

    @Override
    public Collection<? extends UserGroup> getFilterGroups(Collection<? extends UserGroup> groups) {
        Collection<UserGroupEntity> result = new HashSet<>();
        Iterator iterator = groups.iterator();
        while (iterator.hasNext()) {
            UserGroupEntity userGroupEntity = (UserGroupEntity) iterator.next();
            if (!userGroupEntity.isEnable() || !userGroupEntity.isValid()) {
                continue;
            }
            result.add(userGroupEntity);
        }
        return result;
    }

    @Override
    public UserGroup get(String id) {
        return repository.findByValidAndId(true, id);
    }

    @Override
    public UserGroup save(UserGroup group) {
        return repository.save((UserGroupEntity) group);
    }

    @Override
    public UserGroup save(Map<String, Object> map) {
        String id = String.valueOf(map.get("id"));
        UserGroupEntity menuInfo = new UserGroupEntity();
        // 更新
        if (map.get("id") != null && StringUtil.isNotNull(id)) {
            menuInfo = (UserGroupEntity)this.get(id);
        }
        menuInfo.setName(String.valueOf(map.get("name")));
        menuInfo.setEnable((boolean) map.get("enable"));
        menuInfo.setDescription(String.valueOf(map.get("description")));
        menuInfo.setSeq(Integer.parseInt(String.valueOf(map.get("seq"))));
        return repository.save(menuInfo);
    }

    @Override
    public void delete(UserGroup group) {
        this.deleteByIds(group.getId());
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            List<UserGroupEntity> list = new ArrayList<>(idList.size());
            for (String id : idList) {
                UserGroupEntity tempGroup = repository.findByValidAndId(true, id);
                if (tempGroup == null) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.get.failed"));
                }
                if (!tempGroup.getRoles().isEmpty()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.delete.relation.role"));
                }
                if (!tempGroup.getUsers().isEmpty()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.delete.relation.user"));
                }
                tempGroup.setValid(false);
                tempGroup.setEnable(false);
                list.add(tempGroup);
            }
            repository.save(list);
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends UserGroup> updateEanble(Collection<String> idList, boolean enable) {
        List<UserGroupEntity> groupList = new ArrayList<>(idList.size());
        for (String id : idList) {
            groupList.add(repository.findByValidAndId(true, id));
        }
        for (UserGroupEntity group : groupList) {
            group.setEnable(enable);
        }
        return repository.save(groupList);
    }

    @Override
    public UserGroup saveUserGroupRole(String id, String roleId) {
        UserGroup userGroup = get(id);
        if (userGroup != null) {
            Role role = roleService.get(roleId);
            if (role != null) {
                userGroup.add(role);
                userGroup = save(userGroup);
            }
        }
        return userGroup;
    }

    @Override
    public UserGroup deleteUserGroupRole(String id, String roleId) {
        // TODO 具体实现
        UserGroup userGroup = get(id);
        if (userGroup != null) {
            Role role = roleService.get(roleId);
            if (role != null) {
                userGroup.remove(role);
                userGroup = save(userGroup);
            }
        }
        return userGroup;
    }

    @Override
    public Collection<? extends Role> getGroupRoles(String groupId) {
        UserGroup userGroup = this.get(groupId);
        if (userGroup == null || !userGroup.isEnable()) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.get.failed"));
        }
        return roleService.getFilterRoles(userGroup.getRoles());
    }

    @Override
    public UserGroup addGroupUser(String groupId, String userId) {
        UserGroup userGroup = this.get(groupId);
        if (userGroup != null) {
            User user = userService.get(userId);
            if (user != null) {
                userGroup.add(user);
                userGroup = this.save(userGroup);
            }
        }
        return userGroup;
    }

    @Override
    public UserGroup deleteGroupUser(String groupId, String userId) {
        UserGroup userGroup = this.get(groupId);
        if (userGroup != null) {
            User user = userService.get(userId);
            if (user != null) {
                userGroup.remove(user);
                userGroup = this.save(userGroup);
            }
        }
        return userGroup;
    }

    @Override
    public Collection<? extends User> getGroupUsers(String groupId) {
        UserGroup userGroup = this.get(groupId);
        if (userGroup == null || !userGroup.isEnable()) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.get.failed"));
        }
        return userService.getFilterUsers(userGroup.getUsers());
    }

    @Override
    public boolean hasPermissionOfUserGroup(UserGroup userGroup, String reqUrl, RequestMethod requestMethod) {
        if (userGroup == null || !userGroup.isEnable() || !userGroup.isValid()) {
            return false;
        }
        Collection<RoleEntity> roleEntities = (Collection<RoleEntity>) userGroup.getRoles();
        for (RoleEntity roleEntity : roleEntities) {
            if (roleService.hasPermissionOfRole(roleEntity, reqUrl, requestMethod)) {
                return true;
            }
        }
        return false;
    }
}
