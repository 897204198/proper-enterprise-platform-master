package com.proper.enterprise.platform.auth.neo4j.repository;

import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.auth.neo4j.entity.ResourceNodeEntity;
import com.proper.enterprise.platform.core.neo4j.repository.BaseNeo4jRepository;

import java.util.Collection;

public interface ResourceNodeRepository extends BaseNeo4jRepository<ResourceNodeEntity, String> {
    Collection<? extends Resource> save(Collection<ResourceNodeEntity> menuList);

    ResourceNodeEntity findByIdAndValid(String id, boolean valid);

}
