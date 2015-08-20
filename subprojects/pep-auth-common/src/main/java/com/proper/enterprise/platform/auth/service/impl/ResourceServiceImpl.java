package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.dto.ResourceDTO;
import com.proper.enterprise.platform.auth.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    ResourceRepository repo;

    @Override
    public Set<Resource> getAllResources() {
        List<ResourceEntity> list = repo.findAll();
        Set<Resource> resources = new HashSet<>(list.size());
        for (ResourceEntity res : list) {
            resources.add(new ResourceDTO(res));
        }
        return resources;
    }

}
