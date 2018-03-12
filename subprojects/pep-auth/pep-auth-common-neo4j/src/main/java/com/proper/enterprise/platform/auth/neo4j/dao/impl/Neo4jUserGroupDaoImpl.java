package com.proper.enterprise.platform.auth.neo4j.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.auth.neo4j.entity.UserGroupNodeEntity;
import com.proper.enterprise.platform.auth.neo4j.repository.UserGroupNodeRepository;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class Neo4jUserGroupDaoImpl implements UserGroupDao {

    @Autowired
    private UserGroupNodeRepository userGroupNodeRepository;

    @Autowired
    private Session session;

    @Override
    public UserGroup get(String id) {
        return userGroupNodeRepository.findByIdAndValid(id, true);
    }

    @Override
    public Collection<? extends UserGroup> findAll(Collection<String> idList) {
        return (Collection<? extends UserGroup>)userGroupNodeRepository.findAll(idList);
    }

    @Override
    public UserGroup findByValidAndName(boolean valid, String name) {
        return userGroupNodeRepository.findByValidAndName(true, name);
    }

    @Override
    public UserGroup findByValidAndId(boolean valid, String id) {
        return userGroupNodeRepository.findByIdAndValid(id, true);
    }

    @Override
    public UserGroup getNewUserGroup() {
        return new UserGroupNodeEntity();
    }

    @Override
    public UserGroup save(UserGroup group) {
        return userGroupNodeRepository.save((UserGroupNodeEntity) group);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends UserGroup> save(Collection<? extends UserGroup> groups) {
        return userGroupNodeRepository.save((Collection<UserGroupNodeEntity>) groups);
    }

    @Override
    public Collection<? extends UserGroup> getGroups(String name, String description, String enable) {

        Filters filters = new Filters();
        if (StringUtil.isNotBlank(name)) {
            filters.add(new Filter("name", ComparisonOperator.CONTAINING, name));
        }
        if (StringUtil.isNotBlank(description)) {
            Filter filter = new Filter("description", description);
            filter.setComparisonOperator(ComparisonOperator.CONTAINING);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }
        if (StringUtil.isNotBlank(enable)) {
            boolean ok = true;
            if ("n".equalsIgnoreCase(enable)) {
                ok = false;
            }
            Filter filter = new Filter("enable", ok);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }

        Filter filter = new Filter("valid", true);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);

        SortOrder sortOrder = new SortOrder();
        sortOrder.add(SortOrder.Direction.ASC, "seq");

        Collection collection = session.loadAll(UserGroupNodeEntity.class, filters, sortOrder);
        return collection;
    }
}
