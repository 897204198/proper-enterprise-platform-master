package com.proper.enterprise.platform.auth.common.service.impl;

import com.google.common.collect.Lists;
import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository;
import com.proper.enterprise.platform.auth.common.repository.UserRepository;
import com.proper.enterprise.platform.core.enums.ResourceType;
import com.proper.enterprise.platform.core.repository.SearchCondition;
import com.proper.enterprise.platform.core.repository.SearchConditionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 通用的/抽象的用户服务接口实现
 *
 * 其中，获得当前用户的方法由于与安全框架具体实现关联，只能提供抽象实现
 */
public abstract class AbstractUserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUserServiceImpl.class);

    @Autowired
    UserRepository userRepo;

    @Autowired
    ResourceRepository resRepo;

    @Override
    public abstract User getCurrentUser();

    @Override
    public void addUser(User... users) {
        if (users == null) {
            LOGGER.debug("Pass in users array SHOULD NOT NULL!");
            return;
        }
        List<UserEntity> entities = Lists.newArrayListWithCapacity(users.length);
        for (User user : users) {
            entities.add(new UserEntity(user.getUsername(), user.getPassword()));
        }
        userRepo.save(entities);
    }

    @Override
    public User getUser(String username) {
        UserEntity entity = userRepo.findByUsername(username);
        if (entity == null) {
            LOGGER.debug("User with username '{}' is not exist!", username);
        }
        return entity;
    }

    @Override
    public Collection<? extends Resource> getResources() {
        return getResources(getCurrentUser().getUsername());
    }

    @Override
    public Collection<? extends Resource> getResources(String username) {
        UserEntity userEntity = userRepo.findByUsername(username);
        return getResources(userEntity);
    }

    private  Collection<? extends Resource> getResources(UserEntity userEntity) {
        if (userEntity != null) {
            return resRepo.findAll(userEntity.getRoles());
        } else {
            return new ArrayList<Resource>();
        }
    }

    @Override
    public Collection<? extends Resource> getResources(ResourceType resourceType) {
        UserEntity userEntity = userRepo.findOne(getCurrentUser().getId());
        SearchCondition scRole = new SearchCondition("roles", SearchCondition.Operator.IN, userEntity.getRoles());
        SearchCondition scType = new SearchCondition("resourceType", SearchCondition.Operator.EQ, resourceType);
        return resRepo.findAll(SearchConditionBuilder.build(ResourceEntity.class, scRole, scType));
    }

    @Override
    public Collection<? extends Resource> getResourcesById(String userId) {
        UserEntity userEntity = userRepo.findOne(userId);
        return getResources(userEntity);
    }

}
