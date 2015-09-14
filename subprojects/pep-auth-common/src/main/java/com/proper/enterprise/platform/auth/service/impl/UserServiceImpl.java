package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.dto.ResourceDTO;
import com.proper.enterprise.platform.auth.dto.UserDTO;
import com.proper.enterprise.platform.auth.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.entity.RoleEntity;
import com.proper.enterprise.platform.auth.entity.UserEntity;
import com.proper.enterprise.platform.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    UserRepository repo;

    @Override
    public User getUserByUsername(String username) {
        UserEntity entity = repo.findByLoginName(username);
        if (entity == null) {
            LOGGER.debug("User with username '{}' is not exist!", username);
        }
        return new UserDTO(entity);
    }

    @Override
    public Collection<Resource> getUserResources(String userId) {
        UserEntity userEntity = repo.findOne(userId);
        return getResources(userEntity);
    }

    private Collection<Resource> getResources(UserEntity userEntity) {
        Collection<Resource> resources = new HashSet<>();
        if (userEntity != null) {
            for (RoleEntity roleEntity : userEntity.getRoles()) {
                for (ResourceEntity resEntity : roleEntity.getResources()) {
                    resources.add(new ResourceDTO(resEntity));
                }
            }
        }
        return resources;
    }

    @Override
    public Collection<Resource> getUserResourcesByUsername(String username) {
        UserEntity userEntity = repo.findByLoginName(username);
        return getResources(userEntity);
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : principal.toString();
        return getUserByUsername(username);
    }

}
