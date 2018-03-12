package com.proper.enterprise.platform.auth.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.MenuDao;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.auth.jpa.entity.MenuEntity;
import com.proper.enterprise.platform.auth.jpa.repository.MenuRepository;
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
public class MenuDaoImpl implements MenuDao {

    @Autowired
    private MenuRepository repository;

    @Override
    public Menu get(String id) {
        return repository.findOne(id);
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
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> save(Collection<? extends Menu> menus) {
        return repository.save((Collection<MenuEntity>)menus);
    }

    @Override
    public Menu getNewMenuEntity() {
        return new MenuEntity();
    }

    @Override
    public Collection<? extends Menu> findAll() {
        return repository.findAll();
    }

    @Override
    public Collection<? extends Menu> findAll(Collection<String> idList) {
        return repository.findAll(idList);
    }

    @Override
    public Collection<? extends Menu> findAll(Sort sort) {
        return repository.findAll(new Sort("parent", "sequenceNumber"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> getMenuByCondition(String name, String description, String route, String enable, String parentId) {
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
                if (StringUtil.isNotNull(enable)) {
                    predicates.add(cb.equal(root.get("enable"), enable.equals("Y")));
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
    public void deleteAll() {
        repository.deleteAll();
    }
}
