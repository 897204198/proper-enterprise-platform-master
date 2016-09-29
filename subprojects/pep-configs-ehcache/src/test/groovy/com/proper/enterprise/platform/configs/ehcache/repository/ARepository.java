package com.proper.enterprise.platform.configs.ehcache.repository;

import com.proper.enterprise.platform.configs.ehcache.entity.AEntity;
import com.proper.enterprise.platform.core.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;

public interface ARepository extends BaseRepository<AEntity, String> {

    @CacheQuery
    AEntity findByUsername(String username);

}
