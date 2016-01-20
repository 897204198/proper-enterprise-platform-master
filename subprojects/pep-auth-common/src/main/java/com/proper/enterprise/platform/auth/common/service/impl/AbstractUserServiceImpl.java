package com.proper.enterprise.platform.auth.common.service.impl;

import com.google.common.collect.Lists;
import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.dto.ResourceDTO;
import com.proper.enterprise.platform.auth.common.dto.UserDTO;
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository;
import com.proper.enterprise.platform.auth.common.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractUserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUserServiceImpl.class);

    @Autowired
    UserRepository userRepo;

    @Autowired
    ResourceRepository resRepo;

    @Override
    public User getUser(String username) {
        UserEntity entity = userRepo.findByLoginName(username);
        if (entity == null) {
            LOGGER.debug("User with username '{}' is not exist!", username);
        }
        return new UserDTO(entity);
    }

    @Override
    public Collection<Resource> getResourcesById(String userId) {
        UserEntity userEntity = userRepo.findOne(userId);
        return getResources(userEntity);
    }

    private Collection<Resource> getResources(UserEntity userEntity) {
        Collection<Resource> resources = new HashSet<Resource>();
        if (userEntity != null) {
            for (ResourceEntity resEntity : resRepo.findByRoles(userEntity.getRoles())) {
                resources.add(new ResourceDTO(resEntity));
            }
        }
        return resources;
    }

    @Override
    public Collection<Resource> getResources(String username) {
        UserEntity userEntity = userRepo.findByLoginName(username);
        return getResources(userEntity);
    }

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

}
