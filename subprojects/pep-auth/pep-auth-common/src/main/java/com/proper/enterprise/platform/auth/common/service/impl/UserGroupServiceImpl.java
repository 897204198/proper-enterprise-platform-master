package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity;
import com.proper.enterprise.platform.auth.common.repository.UserGroupRepository;
import com.proper.enterprise.platform.core.utils.StringUtil;
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
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupRepository repository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

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
    public UserGroup get(String id) {
        return repository.findOne(id);
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
        repository.delete((UserGroupEntity) group);
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            // TODO 对删除业务进行逻辑判断
            repository.delete(repository.findAll(idList));
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends UserGroup> updateEanble(Collection<String> idList, boolean enable) {
        // TODO 具体实现
        List<UserGroupEntity> groupList = repository.findAll(idList);
        for (UserGroupEntity group : groupList) {
            group.setEnable(enable);
        }
        return repository.save(groupList);
    }

    @Override
    public UserGroup saveUserGroupRole(String id, String roleId) {
        // TODO 具体实现
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

}
