package com.proper.enterprise.platform.auth.common.repository;

import com.proper.enterprise.platform.auth.common.entity.RoleEntity;
import com.proper.enterprise.platform.core.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;

public interface RoleRepository extends BaseRepository<RoleEntity, String> {

    @CacheQuery
    RoleEntity findByCode(String code);

}
