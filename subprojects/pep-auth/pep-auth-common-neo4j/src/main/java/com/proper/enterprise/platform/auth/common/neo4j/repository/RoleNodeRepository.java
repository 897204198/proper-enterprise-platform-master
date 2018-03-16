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

    Collection<RoleNodeEntity> save(List<RoleNodeEntity> roleList);

    Collection<RoleNodeEntity> save(Collection<RoleNodeEntity> roles);

    Collection<RoleNodeEntity> findByNameAndValidAndEnable(String name, boolean valid, boolean enable);

    Collection<RoleNodeEntity> findAllByValidTrueAndEnableTrue();

    Collection<RoleNodeEntity> findAllByNameLike(String name);

    Collection<RoleNodeEntity> findAllByIdIn(Specification specification, Sort name);

    @Query("match (n:PEP_AUTH_ROLES)-[r:parent_to*]->(m:PEP_AUTH_ROLES) where n.id = {id} and m.valid = {valid} and m.enable = {enable} return "
        + "distinct m")
    Collection<RoleNodeEntity> findParentRolesByIdAndValidAndEnable(@Param("id") String id, @Param("valid") boolean valid, @Param("enable") boolean
        enable);

    @Query("match (n:PEP_AUTH_ROLES)-[r:parent_to*]->(m:PEP_AUTH_ROLES) where n.id ={id} and m.id = n.id return case when m is null then false else "
        + "true end;")
    Boolean hasCircleInheritForCurrentRole(@Param("id") String id);

    @Query("match (n:PEP_AUTH_ROLES)-[*]-(m:PEP_AUTH_USERGROUPS) where n.id = {id} and m.valid = true and m.enable = true return distinct m;")
    Collection<UserGroup> findUsergroupsByRoleId(@Param("id") String id);

}
