package com.proper.enterprise.platform.auth.common.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.MenuDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.auth.common.jpa.repository.MenuRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
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
public class MenuDaoImpl extends AbstractJpaServiceSupport<Menu, MenuRepository, String> implements MenuDao {

    private static final String SUPER_PARENTID = "-1";

    @Autowired
    private MenuRepository repository;

    @Override
    public MenuRepository getRepository() {
        return repository;
    }

    @Override
    public Menu get(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> findParents(EnableEnum enable) {
        Specification<Menu> specification = new Specification<Menu>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (null != enable && EnableEnum.ALL != enable) {
                    predicates.add(cb.equal(root.get("enable"), enable == EnableEnum.ENABLE));
                }
                predicates.add(cb.isNotEmpty(root.get("children")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return findAll(specification, Sort.by("parent", "sequenceNumber"));
    }

    @Override
    public Collection<? extends Menu> findAllEq(String name, String route) {
        Specification<Menu> specification = new Specification<Menu>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtil.isNotNull(name)) {
                    predicates.add(cb.equal(root.get("name"), name));
                }
                if (StringUtil.isNotNull(route)) {
                    predicates.add(cb.equal(root.get("route"), route));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return super.findAll(specification);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> findAll(String name, String description, String route, EnableEnum enable, String parentId) {
        return super.findAll(buildUserSpecification(name, description, route, enable, parentId), Sort.by("parent", "sequenceNumber"));
    }

    @Override
    public DataTrunk<? extends Menu> findPage(String name, String description, String route, EnableEnum enable, String parentId) {
        return this.findPage(buildUserSpecification(name, description, route, enable, parentId), Sort.by("parent", "sequenceNumber"));
    }

    @SuppressWarnings("unchecked")
    private Specification<Menu> buildUserSpecification(String name, String description, String route, EnableEnum enable, String parentId) {
        Specification<Menu> specification = new Specification<Menu>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtil.isNotNull(name)) {
                    predicates.add(cb.like(root.get("name"), "%".concat(name).concat("%")));
                }
                if (StringUtil.isNotNull(description)) {
                    predicates.add(cb.like(root.get("description"), "%".concat(description).concat("%")));
                }
                if (StringUtil.isNotNull(route)) {
                    predicates.add(cb.like(root.get("route"), "%".concat(route).concat("%")));
                }
                if (null != enable && EnableEnum.ALL != enable) {
                    predicates.add(cb.equal(root.get("enable"), enable == EnableEnum.ENABLE));
                }
                if (StringUtil.isNotNull(parentId) && !SUPER_PARENTID.equals(parentId)) {
                    predicates.add(cb.equal(root.get("parent").get("id"), parentId));
                } else if (SUPER_PARENTID.equals(parentId)) {
                    predicates.add(cb.isNull(root.get("parent").get("id")));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return specification;
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public Menu updateForSelective(Menu menu) {
        return super.updateForSelective(menu);
    }


}
