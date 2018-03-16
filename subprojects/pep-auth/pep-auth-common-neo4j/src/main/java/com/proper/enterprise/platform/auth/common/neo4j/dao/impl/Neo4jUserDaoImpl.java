package com.proper.enterprise.platform.auth.common.neo4j.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.MenuDao;
import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity;
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserNodeRepository;
import com.proper.enterprise.platform.auth.common.neo4j.entity.MenuNodeEntity;
import com.proper.enterprise.platform.auth.common.neo4j.entity.ResourceNodeEntity;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.neo4j.service.impl.Neo4jServiceSupport;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.sort.BeanComparator;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Service
public class Neo4jUserDaoImpl extends Neo4jServiceSupport<User, UserNodeRepository, String> implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Neo4jUserDaoImpl.class);

    @Autowired
    private UserNodeRepository userNodeRepository;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private MenuDao menuDao;

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
    public DataTrunk<? extends User> getUsersByCondition(String userName, String name, String email, String phone, String enable) {
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
        sortOrder.add(SortOrder.Direction.ASC, "name");
        return this.findPage(UserNodeEntity.class, filters, sortOrder);
    }

    private Collection<ResourceNodeEntity> getResourcesByUserIdAndValidAndEnable(String userId, boolean valid, boolean enable) {
        return userNodeRepository.getResourcesByIdAndValidAndEnable(userId, valid, enable);
    }

    @Override
    public boolean hasPermissionOfUser(User user, String reqUrl, RequestMethod requestMethod) {
        Collection<ResourceNodeEntity> collection = this.getResourcesByUserIdAndValidAndEnable(user.getId(), true, true);
        for (ResourceNodeEntity resourceNodeEntity : collection) {
            if (resourceService.hasPermissionOfResource(resourceNodeEntity, reqUrl, requestMethod)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<? extends Menu> getMenus(User user) {
        Assert.notNull(user, "Could NOT get menus WITHOUT a user");
        if (user.isSuperuser()) {
            return menuDao.findAll(new Sort("parent", "sequenceNumber"));
        }
        List<MenuNodeEntity> menus = (List<MenuNodeEntity>) userNodeRepository.findMenusById(user.getId());
        Collections.sort(menus, new BeanComparator("parent", "sequenceNumber"));
        return menus;
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
