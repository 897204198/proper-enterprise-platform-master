package com.proper.enterprise.platform.auth.common.neo4j.repository;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity;
import com.proper.enterprise.platform.auth.common.neo4j.entity.MenuNodeEntity;
import com.proper.enterprise.platform.auth.common.neo4j.entity.ResourceNodeEntity;
import com.proper.enterprise.platform.core.neo4j.repository.BaseNeo4jRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserNodeRepository extends BaseNeo4jRepository<UserNodeEntity, String> {

    @Query("match (n:PEP_AUTH_USERS)-[*]-(m:PEP_AUTH_RESOURCES) where n.id = {id} and m.valid = {valid} and m.enable = {enable} return distinct m")
    Collection<ResourceNodeEntity> getResourcesByIdAndValidAndEnable(@Param("id") String id, @Param("valid") boolean valid, @Param("enable") boolean
        enable);

    UserNodeEntity findByIdAndValid(String id, boolean valid);

    UserNodeEntity findByIdAndValidAndEnable(String id, boolean valid, boolean enable);

    UserNodeEntity findByUsernameAndValidTrueAndEnableTrue(String username);

    List<UserNodeEntity> findAllByIdIn(Collection<String> ids);

    @Query("match (n:PEP_AUTH_USERS)-[*]-(m:PEP_AUTH_MENUS) where n.id = {id} return distinct m")
    Collection<MenuNodeEntity> findMenusById(@Param("id") String id);

    @Query("match (n:PEP_AUTH_USERS) where n.username =~ {username} or n.name =~ {name} or n.phone =~ {phone} and n.valid = "
        + "true and n.enable = true return n order by n.name")
    Collection<UserNodeEntity> findByUsernameLikeOrNameLikeOrPhoneLikeAndValidTrueAndEnableTrueOrderByNameDesc(@Param("username") String username,
                                                                                                               @Param("name") String name,
                                                                                                               @Param("phone") String phone);

    Collection<? extends User> save(Collection<UserNodeEntity> users);
}
