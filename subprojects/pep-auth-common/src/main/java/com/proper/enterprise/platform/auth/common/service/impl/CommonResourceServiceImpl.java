package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.enums.ResourceType;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository;
import com.proper.enterprise.platform.core.converter.InterfaceCollectionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CommonResourceServiceImpl implements ResourceService {

    @Autowired
    ResourceRepository resourceRepository;

    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save((ResourceEntity) resource);
    }

    @Override
    public Resource get(String id) {
        return resourceRepository.findOne(id);
    }

    @Override
    public void delete(Resource resource) {
        resourceRepository.delete((ResourceEntity) resource);
    }

    @Override
    public Collection<Resource> find() {
        return InterfaceCollectionConverter.convert(resourceRepository.findAll());
    }

    @Override
    public Collection<Resource> find(ResourceType type) {
        return resourceRepository.findByResourceType(type);
    }

}
