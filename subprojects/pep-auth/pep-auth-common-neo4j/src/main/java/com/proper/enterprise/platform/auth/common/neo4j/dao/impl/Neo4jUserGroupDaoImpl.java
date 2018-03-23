package com.proper.enterprise.platform.auth.common.neo4j.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserGroupNodeEntity;
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserGroupNodeRepository;
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

import java.util.Collection;

@Service
public class Neo4jUserGroupDaoImpl extends Neo4jServiceSupport<UserGroup, UserGroupNodeRepository, String> implements UserGroupDao {

    @Autowired
    private UserGroupNodeRepository userGroupNodeRepository;

    @Autowired
    private Session session;

    @Override
    public UserGroup get(String id) {
        return userGroupNodeRepository.findByIdAndValid(id, true);
    }

    @Override
    public UserGroup get(String id, EnableEnum enable) {
        switch (enable) {
            case ALL:
                return userGroupNodeRepository.findByIdAndValid(id, true);
            case DISABLE:
                return userGroupNodeRepository.findByIdAndValidAndEnable(id, true, false);
            case ENABLE:
            default:
                return userGroupNodeRepository.findByIdAndValidAndEnable(id, true, true);
        }
    }

    @Override
    public Collection<? extends UserGroup> findAll(Collection<String> idList) {
        return (Collection<? extends UserGroup>) userGroupNodeRepository.findAll(idList);
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
    public Collection<? extends UserGroup> getGroups(String name, String description, EnableEnum enable) {
        SortOrder sortOrder = new SortOrder();
        sortOrder.add(SortOrder.Direction.ASC, "seq");
        return this.findAll(UserGroupNodeEntity.class, buildFilters(name, description, enable), sortOrder);
    }

    @Override
    public DataTrunk<? extends UserGroup> getGroupsPagniation(String name, String description, EnableEnum enable) {
        SortOrder sortOrder = new SortOrder();
        sortOrder.add(SortOrder.Direction.ASC, "seq");
        return this.findPage(UserGroupNodeEntity.class, buildFilters(name, description, enable), sortOrder);
    }

    private Filters buildFilters(String name, String description, EnableEnum enable) {
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
        if (null != enable && EnableEnum.ALL != enable) {
            Filter filter = new Filter("enable", EnableEnum.ENABLE == enable);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }
        Filter filter = new Filter("valid", true);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);
        return filters;
    }

    @Override
    public UserGroupNodeRepository getRepository() {
        return userGroupNodeRepository;
    }
}
