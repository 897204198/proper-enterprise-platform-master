package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.dto.ResourceDTO;
import com.proper.enterprise.platform.auth.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.entity.RoleEntity;
import com.proper.enterprise.platform.auth.entity.UserEntity;
import com.proper.enterprise.platform.auth.repository.ResourceRepository;
import com.proper.enterprise.platform.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    UserRepository userRepo;
    
    @Autowired
    ResourceRepository resRepo;

    @Override
    public Set<Resource> getResourcesByUser(String userId) {
        UserEntity userEntity = userRepo.findOne(userId);
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

    @Override
    public Set<Resource> getAllResources() {
        List<ResourceEntity> list = resRepo.findAll();
        Set<Resource> resources = new HashSet<>(list.size());
        for (ResourceEntity res : list) {
            resources.add(new ResourceDTO(res));
        }
        return resources;
    }

}
