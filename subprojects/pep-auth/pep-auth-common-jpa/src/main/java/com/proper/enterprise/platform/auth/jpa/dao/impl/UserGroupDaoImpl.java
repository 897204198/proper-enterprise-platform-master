package com.proper.enterprise.platform.auth.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.auth.jpa.entity.UserGroupEntity;
import com.proper.enterprise.platform.auth.jpa.repository.UserGroupRepository;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserGroupDaoImpl implements UserGroupDao {

    @Autowired
    private UserGroupRepository repository;

    @Override
    public UserGroup get(String id) {
        return repository.findByValidAndId(true, id);
    }

    @Override
    public Collection<? extends UserGroup> findAll(Collection<String> idList) {
        return repository.findAll(idList);
    }

    @Override
    public UserGroup findByValidAndName(boolean valid, String name) {
        return repository.findByValidAndName(true, name);
    }

    @Override
    public UserGroup findByValidAndId(boolean valid, String id) {
        return repository.findByValidAndId(true, id);
    }

    @Override
    public UserGroup getNewUserGroup() {
        return new UserGroupEntity();
    }

    @Override
    public UserGroup save(UserGroup group) {
        return repository.save((UserGroupEntity) group);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends UserGroup> save(Collection<? extends UserGroup> groups) {
        return repository.save((Collection<UserGroupEntity>) groups);
    }

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
}
