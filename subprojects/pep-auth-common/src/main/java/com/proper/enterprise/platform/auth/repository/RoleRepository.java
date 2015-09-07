package com.proper.enterprise.platform.auth.repository;

import com.proper.enterprise.platform.auth.entity.RoleEntity;
import com.proper.enterprise.platform.core.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;

public interface RoleRepository extends BaseRepository<RoleEntity, String> {

    @CacheQuery
    RoleEntity findByCode(String code);

}
