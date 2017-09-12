package com.proper.enterprise.platform.cache.ehcache.repository;

import com.proper.enterprise.platform.cache.ehcache.entity.AnEntity;
import com.proper.enterprise.platform.core.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;

public interface AnRepository extends BaseRepository<AnEntity, String> {

    @CacheQuery
    AnEntity findByUsername(String username);

}
