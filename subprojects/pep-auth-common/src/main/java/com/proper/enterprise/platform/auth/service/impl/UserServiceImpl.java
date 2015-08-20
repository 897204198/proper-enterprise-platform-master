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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    UserRepository repo;

    @Override
    public User getUserByUsername(String username) {
        return new UserDTO(repo.findByLoginName(username));
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

}
