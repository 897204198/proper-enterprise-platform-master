package com.proper.enterprise.platform.auth.common.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.MenuDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity;
import com.proper.enterprise.platform.auth.common.jpa.repository.MenuRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.impl.JpaServiceSupport;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.sort.BeanComparator;
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
                return repository.findOne(id);
            case DISABLE:
                return repository.findByIdAndEnable(id, false);
            case ENABLE:
            default:
                return repository.findByIdAndEnable(id, true);
        }
    }

    @Override
    public Collection<? extends Menu> getMenus(User user) {
        List<Menu> result = new ArrayList<>(0);
        if (user.isEnable()) {
            Set<Menu> menus = new HashSet<>();
            menus = addRoleMenus(user.getRoles(), menus);
            for (UserGroup userGroup : user.getUserGroups()) {
                if (userGroup.isEnable()) {
                    menus = addRoleMenus(userGroup.getRoles(), menus);
                }
            }
            result = new ArrayList<>(menus.size());
            result.addAll(menus);
            result.sort(new BeanComparator("parent", "sequenceNumber"));
        }
        return result;
    }

    private Set<Menu> addRoleMenus(Collection<? extends Role> roles, Set<Menu> menus) {
        for (Role role : roles) {
            if (role.isEnable()) {
                Collection<? extends Menu> userMenus = role.getMenus();
                for (Menu menu : userMenus) {
                    if (menu.isEnable()) {
                        menus.add(menu);
                    }
                }
            }
        }
        return menus;
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
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return repository.findAll(specification, new Sort("parent", "sequenceNumber"));
    }

    @Override
    public DataTrunk<? extends Menu> findMenusPagniation(String name, String description, String route, EnableEnum enable, String parentId) {
        return this.findPage(buildUserSpecification(name, description, route, enable, parentId), new Sort("parent", "sequenceNumber"));
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
                if (StringUtil.isNotNull(parentId) && !"-1".equals(parentId)) {
                    predicates.add(cb.equal(root.get("parent").get("id"), parentId));
                } else if ("-1".equals(parentId)) {
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
}
