package com.proper.enterprise.platform.auth.neo4j.repository;

import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.auth.neo4j.entity.ResourceNodeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Collection;

public interface ResourceNodeRepository extends Neo4jRepository<ResourceNodeEntity, String> {
    Collection<? extends Resource> save(Collection<ResourceNodeEntity> menuList);

    ResourceNodeEntity findByIdAndValid(String id, boolean valid);

}
