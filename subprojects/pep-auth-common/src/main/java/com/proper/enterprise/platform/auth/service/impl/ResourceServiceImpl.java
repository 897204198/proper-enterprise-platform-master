package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.dto.ResourceDTO;
import com.proper.enterprise.platform.auth.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.entity.RoleResourceEntity;
import com.proper.enterprise.platform.auth.entity.UserRoleEntity;
import com.proper.enterprise.platform.auth.repository.ResourceRepository;
import com.proper.enterprise.platform.auth.repository.RoleResourceRepository;
import com.proper.enterprise.platform.auth.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ResourceServiceImpl implements ResourceService {
    
    @Autowired
    UserRoleRepository urRepo;
    
    @Autowired
    RoleResourceRepository rrRepo;

    @Autowired
    ResourceRepository resRepo;

    @Override
    public Set<Resource> getResourcesByUser(String userId) {
        List<UserRoleEntity> ures = urRepo.findAllByUserId(userId);
        int len = ures.size();
        Set<String> roles = new HashSet<>(len);
        for (UserRoleEntity ure : ures) {
            roles.add(ure.getRoleId());
        }
        List<RoleResourceEntity> rres = rrRepo.findByRoleIdIn(roles);
        Set<Resource> resources = new HashSet<>(rres.size());
        for (RoleResourceEntity rre : rres) {
            resources.add(new ResourceDTO(resRepo.findOne(rre.getResourceId())));
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
