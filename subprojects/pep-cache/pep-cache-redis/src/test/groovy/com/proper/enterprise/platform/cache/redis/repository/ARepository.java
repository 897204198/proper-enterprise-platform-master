package com.proper.enterprise.platform.cache.redis.repository;

import com.proper.enterprise.platform.cache.redis.entity.AEntity;
import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;

import java.util.Collection;

public interface ARepository extends BaseRepository<AEntity, String> {

    @CacheQuery
    AEntity findByUsername(String username);

    Collection<AEntity> findByUsernameLike(String username);

}
