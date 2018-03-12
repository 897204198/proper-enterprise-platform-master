package com.proper.enterprise.platform.auth.neo4j.repository;

import com.proper.enterprise.platform.auth.neo4j.entity.UserGroupNodeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Collection;

public interface UserGroupNodeRepository extends Neo4jRepository<UserGroupNodeEntity, String> {

    UserGroupNodeEntity findByIdAndValid(String id, boolean valid);

    UserGroupNodeEntity findByValidAndName(boolean valid, String name);

    Collection<UserGroupNodeEntity> save(Collection<UserGroupNodeEntity> groups);
}
