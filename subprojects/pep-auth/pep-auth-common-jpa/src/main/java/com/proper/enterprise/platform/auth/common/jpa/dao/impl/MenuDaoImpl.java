package com.proper.enterprise.platform.auth.common.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.MenuDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity;
import com.proper.enterprise.platform.auth.common.jpa.repository.MenuRepository;
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
public class MenuDaoImpl extends JpaServiceSupport<Menu, MenuRepository, String> implements MenuDao {

    @Autowired
    private MenuRepository repository;

    @Override
    public MenuRepository getRepository() {
        return repository;
    }

    @Override
    public Menu get(String id) {
        return repository.findOne(id);
    }

    @Override
    public Menu get(String id, EnableEnum enableEnum) {
        switch (enableEnum) {
            case ALL:
                return repository.findByIdAndValid(id, true);
            case DISABLE:
                return repository.findByIdAndValidAndEnable(id, true, false);
            case ENABLE:
            default:
                return repository.findByIdAndValidAndEnable(id, true, true);
        }
    }

    @Override
    public Collection<? extends Menu> getByIds(Collection<String> ids) {
        return repository.findAll(ids);
    }

    @Override
    public Menu save(Menu menu) {
        return repository.save((MenuEntity) menu);
    }

    @Override
    public Menu getNewMenuEntity() {
        return new MenuEntity();
    }

    @Override
    public Collection<? extends Menu> findAll(Collection<String> idList) {
        return repository.findAll(idList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> getMenuByCondition(String name, String description, String route, EnableEnum enable, String parentId) {
        Specification specification = new Specification<MenuEntity>() {
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
                if (StringUtil.isNotNull(parentId)) {
                    predicates.add(cb.equal(root.get("parent").get("id"), parentId));
                }
                predicates.add(cb.equal(root.get("valid"), true));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return repository.findAll(specification, new Sort("parent", "sequenceNumber"));
    }

    @Override
    public DataTrunk<? extends Menu> findMenusPagniation(String name, String description, String route, EnableEnum enable, String parentId) {
        return this.findPage(buildUserSpecification(name, description, route, enable, parentId), new Sort(Sort.Direction.ASC, "name"));
    }

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
                if (StringUtil.isNotNull(parentId)) {
                    predicates.add(cb.equal(root.get("parent").get("id"), parentId));
                }
                predicates.add(cb.equal(root.get("valid"), true));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return specification;
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
