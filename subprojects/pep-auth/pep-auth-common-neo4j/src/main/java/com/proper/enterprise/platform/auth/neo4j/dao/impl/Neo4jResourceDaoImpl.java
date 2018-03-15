package com.proper.enterprise.platform.auth.neo4j.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.ResourceDao;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.auth.neo4j.entity.ResourceNodeEntity;
import com.proper.enterprise.platform.auth.neo4j.repository.ResourceNodeRepository;
import com.proper.enterprise.platform.core.neo4j.service.impl.Neo4jServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Neo4jResourceDaoImpl extends Neo4jServiceSupport<Resource, ResourceNodeRepository, String> implements ResourceDao {

    @Autowired
    private ResourceNodeRepository resourceNodeRepository;

    @Override
    public ResourceNodeRepository getRepository() {
        return resourceNodeRepository;
    }

    @Override
    public Resource save(Resource resource) {
        return resourceNodeRepository.save((ResourceNodeEntity) resource);
    }

    @Override
    public Resource getNewResourceEntity() {
        return new ResourceNodeEntity();
    }

    @Override
    public Resource get(String id) {
        return resourceNodeRepository.findOne(id);
    }

    @Override
    public Collection<? extends Resource> findAll(Collection<String> ids) {
        return (Collection<? extends Resource>) resourceNodeRepository.findAll(ids);
    }

    @Override
    public void deleteAll() {
        resourceNodeRepository.deleteAll();
    }
}
