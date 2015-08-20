package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.auth.dto.ResourceDTO;
import com.proper.enterprise.platform.auth.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.entity.RoleEntity;
import com.proper.enterprise.platform.auth.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.dto.UserDTO;
import com.proper.enterprise.platform.auth.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    UserRepository repo;

    @Override
    public User getUserByUsername(String username) {
        return new UserDTO(repo.findByLoginName(username));
    }

    @Override
    public Set<Resource> getUserResources(String username) {
        UserEntity userEntity = repo.findByLoginName(username);
        Set<Resource> resources = new HashSet<>();
        if (userEntity != null) {
            for (RoleEntity roleEntity : userEntity.getRoles()) {
                for (ResourceEntity resEntity : roleEntity.getResources()) {
                    resources.add(new ResourceDTO(resEntity));
                }
            }
        }
        return resources;
    }

}
