package com.proper.enterprise.platform.auth.common.neo4j.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.MenuDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.auth.common.neo4j.entity.MenuNodeEntity;
import com.proper.enterprise.platform.auth.common.neo4j.repository.MenuNodeRepository;
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserNodeRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.neo4j.service.impl.Neo4jServiceSupport;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Neo4jMenuDaoImpl extends Neo4jServiceSupport<Menu, MenuNodeRepository, String> implements MenuDao {

    @Autowired
    private MenuNodeRepository menuNodeRepository;

    @Autowired
    private UserNodeRepository userNodeRepository;

    @Override
    public MenuNodeRepository getRepository() {
        return menuNodeRepository;
    }

    @Autowired
    private Session session;

    @Override
    public Menu get(String id) {
        return menuNodeRepository.findOne(id);
    }

    @Override
    public Menu get(String id, EnableEnum enable) {
        switch (enable) {
            case ALL:
                return menuNodeRepository.findByIdAndValid(id, true);
            case DISABLE:
                return menuNodeRepository.findByIdAndValidAndEnable(id, true, false);
            case ENABLE:
            default:
                return menuNodeRepository.findByIdAndValidAndEnable(id, true, true);
        }
    }

    @Override
    public Collection<? extends Menu> getMenus(User user) {
        return userNodeRepository.findMenusById(user.getId());
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
    public Menu getNewMenuEntity() {
        return new MenuNodeEntity();
    }

    @Override
    public Collection<? extends Menu> findAll(Collection<String> idList) {
        return (Collection<? extends Menu>) menuNodeRepository.findAll(idList);
    }

    @Override
    public Collection<? extends Menu> getMenuByCondition(String name, String description, String route, EnableEnum enable, String parentId) {
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
        if (null != enable && EnableEnum.ALL != enable) {
            Filter filter = new Filter("enable", EnableEnum.ENABLE == enable);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }
        Filter filter = new Filter("valid", true);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);

        SortOrder sortOrder = new SortOrder();
        sortOrder.add(SortOrder.Direction.ASC, "sequenceNumber");

        Collection<MenuNodeEntity> collection = session.loadAll(MenuNodeEntity.class, filters, sortOrder);
        Collection<MenuNodeEntity> result = new HashSet<>();
        for (MenuNodeEntity menuNodeEntity : collection) {
            if (StringUtil.equalsIgnoreCase(menuNodeEntity.getParentId(), parentId)) {
                result.add(menuNodeEntity);
            }
        }
        return result;
    }

    @Override
    public DataTrunk<? extends Menu> findMenusPagniation(String name, String description, String route, EnableEnum enable, String parentId) {
        SortOrder sortOrder = new SortOrder();
        sortOrder.add(SortOrder.Direction.ASC, "sequenceNumber");
        return this.findPage(MenuNodeEntity.class, buildUserFilters(name, description, route, enable, parentId), sortOrder);
    }

    private Filters buildUserFilters(String name, String description, String route, EnableEnum enable, String parentId) {
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
        if (null != enable && EnableEnum.ALL != enable) {
            Filter filter = new Filter("enable", EnableEnum.ENABLE == enable);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }
        if (StringUtil.isNotBlank(parentId)) {
            filters.add(new Filter("parentId", ComparisonOperator.CONTAINING, parentId));
        }
        Filter filter = new Filter("valid", true);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);
        return filters;
    }


    @Override
    public void deleteAll() {
        menuNodeRepository.deleteAll();
    }
}
