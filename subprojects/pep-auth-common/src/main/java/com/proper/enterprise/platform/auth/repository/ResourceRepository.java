package com.proper.enterprise.platform.auth.repository;

import com.proper.enterprise.platform.auth.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.entity.RoleEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Set;

public interface ResourceRepository extends BaseRepository<ResourceEntity, String> {

    @Query("SELECT res FROM ResourceEntity res INNER JOIN res.roles r WHERE r IN (?1)")
    Set<ResourceEntity> findByRoles(Collection<RoleEntity> roles);

}
