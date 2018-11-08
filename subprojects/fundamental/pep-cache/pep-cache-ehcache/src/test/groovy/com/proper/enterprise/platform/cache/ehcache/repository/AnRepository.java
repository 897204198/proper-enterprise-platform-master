package com.proper.enterprise.platform.cache.ehcache.repository;

import com.proper.enterprise.platform.cache.ehcache.entity.AnEntity;
import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;

public interface AnRepository extends BaseRepository<AnEntity, String> {

    /**
     * 通过username 取到缓存的entity
     * @param username username
     * @return AnEntity
     */
    @CacheQuery
    AnEntity findByUsername(String username);

}
