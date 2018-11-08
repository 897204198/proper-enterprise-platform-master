package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.AccessTokenEntity;
import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

public interface AccessTokenRepository extends BaseJpaRepository<AccessTokenEntity, String> {

    /**
     * 通过 token 获取 AccessTokenEntity
     * @param token token
     * @return AccessTokenEntity
     */
    @CacheQuery
    AccessTokenEntity getByToken(String token);

    /**
     * 根据 userId 获取 AccessTokenEntity
     * @param userId userId
     * @return AccessTokenEntity
     */
    @CacheQuery
    AccessTokenEntity getByUserId(String userId);

    /**
     * 根据token删除
     * @param token token
     */
    void deleteByToken(String token);

}
