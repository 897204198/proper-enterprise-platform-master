package com.proper.enterprise.platform.auth.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.ResourceDao;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.auth.jpa.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.jpa.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResourceDaoImpl implements ResourceDao {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save((ResourceEntity) resource);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Resource> save(Collection<? extends Resource> resources) {
        return resourceRepository.save((Collection<ResourceEntity>)resources);
    }

    @Override
    public Resource getNewResourceEntity() {
        return new ResourceEntity();
    }

    @Override
    public Resource get(String id) {
        return resourceRepository.findOne(id);
    }

    @Override
    public Collection<? extends Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    public Collection<? extends Resource> findAll(Collection<String> ids) {
        return resourceRepository.findAll(ids);
    }

    @Override
    public void deleteAll() {
        resourceRepository.deleteAll();
    }
}
