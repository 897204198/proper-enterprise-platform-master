package com.proper.enterprise.platform.cache.redis.repository;

import com.proper.enterprise.platform.cache.redis.entity.AEntity;
import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;

import java.util.Collection;

public interface ARepository extends BaseRepository<AEntity, String> {

    /**
     * 根据username查询
     * @param username username
     * @return AEntity
     */
    @CacheQuery
    AEntity findByUsername(String username);

    /**
     * 根据username模糊查询
     * @param username username
     * @return 集合
     */
    Collection<AEntity> findByUsernameLike(String username);

}
