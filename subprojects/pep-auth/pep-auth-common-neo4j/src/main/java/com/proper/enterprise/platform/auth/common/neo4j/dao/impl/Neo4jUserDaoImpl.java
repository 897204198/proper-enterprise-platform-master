package com.proper.enterprise.platform.auth.common.neo4j.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity;
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserNodeRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.neo4j.service.impl.Neo4jServiceSupport;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class Neo4jUserDaoImpl extends Neo4jServiceSupport<User, UserNodeRepository, String> implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Neo4jUserDaoImpl.class);

    @Autowired
    private UserNodeRepository userNodeRepository;

    @Override
    public User save(User user) {
        return userNodeRepository.save((UserNodeEntity) user);
    }

    @Override
    public void save(User... users) {
        List<UserNodeEntity> entities = new ArrayList<>(users.length);
        for (User user : users) {
            entities.add((UserNodeEntity) user);
        }
        userNodeRepository.save(entities);
    }

    @Override
    public User getNewUser() {
        return new UserNodeEntity();
    }

    @Override
    public Collection<? extends User> findAll(Collection<String> idList) {
        return (Collection<? extends User>) userNodeRepository.findAll(idList);
    }

    @Override
    public User findByValidTrueAndId(String id) {
        return userNodeRepository.findByIdAndValid(id, true);
    }

    @Override
    public User get(String id) {
        LOGGER.debug("Get user with {} from DB", id);
        return userNodeRepository.findByIdAndValidAndEnable(id, true, true);
    }


    @Override
    public User get(String id, EnableEnum enable) {
        switch (enable) {
            case ALL:
                return userNodeRepository.findByIdAndValid(id, true);
            case DISABLE:
                return userNodeRepository.findByIdAndValidAndEnable(id, true, false);
            case ENABLE:
            default:
                return userNodeRepository.findByIdAndValidAndEnable(id, true, true);
        }
    }

    @Override
    public User getByUsername(String username) {
        LOGGER.debug("GetByUsername with username {} from DB", username);
        return userNodeRepository.findByUsernameAndValidTrueAndEnableTrue(username);
    }

    @Override
    public Collection<? extends User> getUsersByCondition(String condition) {
        condition = "(?i).*".concat(condition).concat(".*");
        return userNodeRepository.findByUsernameLikeOrNameLikeOrPhoneLikeAndValidTrueAndEnableTrueOrderByNameDesc(condition, condition, condition);
    }


    @Override
    @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
    public Collection<? extends User> getUsersByCondition(String userName, String name, String email, String phone, EnableEnum enable) {
        SortOrder sortOrder = new SortOrder();
        sortOrder.add(SortOrder.Direction.ASC, "name");
        return this.findAll(UserNodeEntity.class, buildUserFilters(userName, name, email, phone, enable), sortOrder);
    }

    @Override
    public DataTrunk<? extends User> findUsersPagniation(String userName, String name, String email, String phone, EnableEnum enable) {
        SortOrder sortOrder = new SortOrder();
        sortOrder.add(SortOrder.Direction.ASC, "name");
        return this.findPage(UserNodeEntity.class, buildUserFilters(userName, name, email, phone, enable), sortOrder);
    }

    private Filters buildUserFilters(String userName, String name, String email, String phone, EnableEnum enable) {
        Filters filters = new Filters();
        if (StringUtil.isNotBlank(userName)) {
            filters.add(new Filter("username", ComparisonOperator.CONTAINING, userName));
        }
        if (StringUtil.isNotBlank(name)) {
            filters.add(new Filter("name", ComparisonOperator.CONTAINING, name));
        }
        if (StringUtil.isNotBlank(email)) {
            filters.add(new Filter("email", ComparisonOperator.CONTAINING, email));
        }
        if (StringUtil.isNotBlank(phone)) {
            filters.add(new Filter("phone", ComparisonOperator.CONTAINING, phone));
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
    public UserNodeRepository getRepository() {
        return userNodeRepository;
    }

    @Override
    public void deleteAll() {
        userNodeRepository.deleteAll();
    }
}
