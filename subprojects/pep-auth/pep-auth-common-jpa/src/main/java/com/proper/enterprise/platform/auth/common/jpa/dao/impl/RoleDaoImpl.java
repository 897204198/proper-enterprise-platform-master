package com.proper.enterprise.platform.auth.common.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity;
import com.proper.enterprise.platform.auth.common.jpa.repository.RoleRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
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
import java.util.LinkedList;
import java.util.List;

@Service
public class RoleDaoImpl extends AbstractJpaServiceSupport<Role, RoleRepository, String> implements RoleDao {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleRepository getRepository() {
        return roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save((RoleEntity) role);
    }

    @Override
    public Role updateForSelective(Role role) {
        return super.updateForSelective(role);
    }

    @Override
    public Role getNewRole() {
        return new RoleEntity();
    }

    @Override
    public Collection<? extends Role> findRoles(Collection<String> idList) {
        return super.findAll(idList);
    }

    @Override
    public Collection<? extends Role> findRoles(EnableEnum enable) {
        return super.findAll(buildRolesSpecification(null, null, null, enable));
    }

    @Override
    public Collection<? extends Role> findRoles(String name, EnableEnum enable) {
        return roleRepository.findByNameAndEnable(name, true);
    }

    @Override
    public Collection<? extends Role> findRolesByParentId(List<String> parentIds) {
        if (CollectionUtil.isEmpty(parentIds)) {
            return new ArrayList<>();
        }
        Specification<Role> roleSpecification = new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                return cb.and(root.get("parent").get("id").in(parentIds));
            }
        };
        return super.findAll(roleSpecification);
    }

    @Override
    public Collection<? extends Role> findRolesLike(String name, EnableEnum enable) {
        return findRolesLike(name, null, null, enable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Role> findRolesLike(String name, String description, String parentId, EnableEnum enable) {
        return super.findAll(buildRolesSpecification(name, description, parentId, enable), new Sort("name"));
    }

    @Override
    public Collection<? extends Role> findParentRoles(String currentRoleId) {
        Collection<Role> result = new LinkedList<>();
        Role role = super.findOne(currentRoleId);
        if (role == null) {
            return new ArrayList<>();
        }
        Role parent = role.getParent();
        while (parent != null && parent.getEnable()) {
            result.add(parent);
            parent = parent.getParent();
        }
        return result;
    }


    @Override
    public DataTrunk<? extends Role> findRolesPagniation(String name, String description, String parentId, EnableEnum enable) {
        return this.findPage(buildRolesSpecification(name, description, parentId, enable), new Sort("name"));
    }

    private Specification<Role> buildRolesSpecification(String name, String description, String parentId, EnableEnum enable) {
        return new Specification<Role>() {
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
                if (null != enable && EnableEnum.ALL != enable) {
                    predicates.add(cb.equal(root.get("enable"), enable == EnableEnum.ENABLE));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
