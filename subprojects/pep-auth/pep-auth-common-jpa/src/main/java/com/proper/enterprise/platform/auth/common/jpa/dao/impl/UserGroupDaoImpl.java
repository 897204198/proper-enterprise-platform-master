package com.proper.enterprise.platform.auth.common.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.auth.common.jpa.entity.UserGroupEntity;
import com.proper.enterprise.platform.auth.common.jpa.repository.UserGroupRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.impl.JpaServiceSupport;
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
public class UserGroupDaoImpl extends JpaServiceSupport<UserGroup, UserGroupRepository, String> implements UserGroupDao {

    @Autowired
    private UserGroupRepository repository;

    @Override
    public UserGroupRepository getRepository() {
        return repository;
    }

    @Override
    public UserGroup get(String id) {
        return repository.findOne(id);
    }

    @Override
    public UserGroup get(String id, EnableEnum enable) {
        switch (enable) {
            case ALL:
                return repository.findOne(id);
            case DISABLE:
                return repository.findByIdAndEnable(id, false);
            case ENABLE:
            default:
                return repository.findByIdAndEnable(id, true);
        }
    }

    @Override
    public Collection<? extends UserGroup> findAll(Collection<String> idList) {
        return repository.findAll(idList);
    }

    @Override
    public UserGroup findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public UserGroup findById(String id) {
        return repository.findOne(id);
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
    public Collection<? extends UserGroup> getGroups(String name, String description, EnableEnum enable) {
        return this.findAll(buildSpecification(name, description, enable), new Sort("seq"));
    }

    @Override
    public DataTrunk<? extends UserGroup> getGroupsPagniation(String name, String description, EnableEnum enable) {
        return this.findPage(buildSpecification(name, description, enable), new Sort("seq"));
    }

    private Specification<UserGroup> buildSpecification(String name, String description, EnableEnum enable) {
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
                if (null != enable && EnableEnum.ALL != enable) {
                    predicates.add(cb.equal(root.get("enable"), enable == EnableEnum.ENABLE));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return specification;
    }
}
