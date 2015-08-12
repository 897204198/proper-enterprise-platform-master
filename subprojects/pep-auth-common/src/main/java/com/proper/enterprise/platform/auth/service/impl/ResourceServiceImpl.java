package com.proper.enterprise.platform.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.ResourceConverter;
import com.proper.enterprise.platform.auth.entity.RoleResourceEntity;
import com.proper.enterprise.platform.auth.entity.UserRoleEntity;
import com.proper.enterprise.platform.auth.repository.RoleResourceRepository;
import com.proper.enterprise.platform.auth.repository.UserRoleRepository;

@Service
public class ResourceServiceImpl implements ResourceService {
    
    @Autowired
    UserRoleRepository urRepo;
    
    @Autowired
    RoleResourceRepository rrRepo;

    @Override
    public List<Resource> getResourcesByUser(String userId) {
        List<UserRoleEntity> ures = urRepo.findAllByUserId(userId);
        int len = ures.size();
        String[] roles = new String[len];
        for (int i = 0; i < len; i++) {
            roles[i] = ures.get(i).getRoleId();
        }
        List<RoleResourceEntity> rres = rrRepo.findAllByRoles(roles);
        List<Resource> resources = new ArrayList<Resource>();
        for (RoleResourceEntity rre : rres) {
            resources.add(ResourceConverter.toResource(rre));
        }
        return resources;
    }

}
