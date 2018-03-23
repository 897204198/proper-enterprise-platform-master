package com.proper.enterprise.platform.auth.common.neo4j.repository;

import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.auth.common.neo4j.entity.RoleNodeEntity;
import com.proper.enterprise.platform.core.neo4j.repository.BaseNeo4jRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface RoleNodeRepository extends BaseNeo4jRepository<RoleNodeEntity, String> {

    RoleNodeEntity findByIdAndValidTrueAndEnableTrue(String id);

    RoleNodeEntity findByIdAndValid(String id, boolean valid);

    RoleNodeEntity findByIdAndValidAndEnable(String id, boolean valid, boolean enable);

    Collection<RoleNodeEntity> save(List<RoleNodeEntity> roleList);

    Collection<RoleNodeEntity> save(Collection<RoleNodeEntity> roles);

    Collection<RoleNodeEntity> findByNameAndValidAndEnable(String name, boolean valid, boolean enable);

    Collection<RoleNodeEntity> findAllByValidTrueAndEnableTrue();

    Collection<RoleNodeEntity> findAllByNameLike(String name);

    Collection<RoleNodeEntity> findAllByIdIn(Specification specification, Sort name);

    @Query("MATCH (n:PEP_AUTH_ROLES)-[r:parent_to*]->(m:PEP_AUTH_ROLES) WHERE n.id = {id} AND m.valid = {valid} AND m.enable = {enable} RETURN "
        + "DISTINCT m")
    Collection<RoleNodeEntity> findParentRolesByIdAndValidAndEnable(@Param("id") String id, @Param("valid") boolean valid, @Param("enable") boolean
        enable);

    @Query("MATCH (n:PEP_AUTH_ROLES)-[r:parent_to*]->(m:PEP_AUTH_ROLES) WHERE n.id ={id} AND m.id = n.id RETURN CASE WHEN m IS NULL THEN false ELSE "
        + "true END;")
    Boolean hasCircleInheritForCurrentRole(@Param("id") String id);

    @Query("MATCH (n:PEP_AUTH_ROLES)-[*]-(m:PEP_AUTH_USERGROUPS) WHERE n.id = {id} AND m.valid = true AND m.enable = true RETURN DISTINCT m;")
    Collection<UserGroup> findUsergroupsByRoleId(@Param("id") String id);

}
