package com.proper.enterprise.platform.auth.common.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.ResourceDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.common.jpa.repository.ResourceRepository;
import com.proper.enterprise.platform.core.jpa.service.impl.JpaServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ResourceDaoImpl extends JpaServiceSupport<Resource, ResourceRepository, String> implements ResourceDao {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public ResourceRepository getRepository() {
        return resourceRepository;
    }

    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save((ResourceEntity) resource);
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
    public Resource get(String id, EnableEnum enable) {
        switch (enable) {
            case ALL:
                return resourceRepository.findOne(id);
            case DISABLE:
                return resourceRepository.findByIdAndEnable(id, false);
            case ENABLE:
            default:
                return resourceRepository.findByIdAndEnable(id, true);
        }
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
