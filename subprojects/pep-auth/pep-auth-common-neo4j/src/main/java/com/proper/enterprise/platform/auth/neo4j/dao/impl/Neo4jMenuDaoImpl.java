package com.proper.enterprise.platform.auth.neo4j.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.MenuDao;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.auth.neo4j.entity.MenuNodeEntity;
import com.proper.enterprise.platform.auth.neo4j.repository.MenuNodeRepository;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Neo4jMenuDaoImpl implements MenuDao {

    @Autowired
    private MenuNodeRepository menuNodeRepository;

    @Autowired
    private Session session;

    @Override
    public Menu get(String id) {
        return menuNodeRepository.findOne(id);
    }

    @Override
    public Collection<? extends Menu> getByIds(Collection<String> ids) {
        return (Collection<MenuNodeEntity>) menuNodeRepository.findAll(ids);
    }

    @Override
    public Menu save(Menu menu) {
        return menuNodeRepository.save((MenuNodeEntity) menu);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> save(Collection<? extends Menu> menus) {
        return menuNodeRepository.save((Collection<MenuNodeEntity>) menus);
    }

    @Override
    public Menu getNewMenuEntity() {
        return new MenuNodeEntity();
    }

    @Override
    public Collection<? extends Menu> findAll() {
        return (Collection<? extends Menu>)menuNodeRepository.findAll();
    }

    @Override
     public Collection<? extends Menu> findAll(Collection<String> idList) {
        return (Collection<? extends Menu>)menuNodeRepository.findAll(idList);
    }

    @Override
    public Collection<? extends Menu> findAll(Sort sort) {
        return (Collection<? extends Menu>) menuNodeRepository.findAll(new Sort("parent", "sequenceNumber"));
    }

    @Override
    public Collection<? extends Menu> getMenuByCondition(String name, String description, String route, String enable, String parentId) {
        Filters filters = new Filters();
        if (StringUtil.isNotBlank(name)) {
            filters.add(new Filter("name", ComparisonOperator.CONTAINING, name));
        }
        if (StringUtil.isNotBlank(description)) {
            filters.add(new Filter("description", ComparisonOperator.CONTAINING, description));
        }
        if (StringUtil.isNotBlank(route)) {
            filters.add(new Filter("route", ComparisonOperator.CONTAINING, route));
        }
        Boolean isEnable = null;
        if ("Y".equalsIgnoreCase(enable)) {
            isEnable = true;
        } else if ("N".equalsIgnoreCase(enable)) {
            isEnable = false;
        }
        if (isEnable != null) {
            Filter filter = new Filter("enable", isEnable);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }
        Filter filter = new Filter("valid", true);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);

        SortOrder sortOrder = new SortOrder();
        sortOrder.add(SortOrder.Direction.ASC, "sequenceNumber");

        Collection<MenuNodeEntity>  collection = session.loadAll(MenuNodeEntity.class, filters, sortOrder);
        Collection<MenuNodeEntity> result = new HashSet<>();
        for (MenuNodeEntity menuNodeEntity:collection) {
            if (StringUtil.equalsIgnoreCase(menuNodeEntity.getParentId(), parentId)) {
                result.add(menuNodeEntity);
            }
        }
        return result;
    }

    @Override
    public void deleteAll() {
        menuNodeRepository.deleteAll();
    }
}
