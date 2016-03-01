package com.proper.enterprise.platform.auth.common.repository;

import com.proper.enterprise.platform.auth.common.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.common.entity.RoleEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ResourceRepository extends BaseRepository<ResourceEntity, String> {

    @Query("SELECT DISTINCT res FROM ResourceEntity res INNER JOIN res.roleEntities r WHERE r IN (?1)")
    Collection<ResourceEntity> findAll(Collection<RoleEntity> roleEntities);

}
