package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity;
import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

public interface UserRepository extends BaseJpaRepository<UserEntity, String> {
    /**
     * 仅用于当前登录人查询
     *
     * @param userId 用户id
     * @return 用户实体
     */
    @CacheQuery
    UserEntity findByIdAndEnableTrue(String userId);

    UserEntity findByUsernameAndEnable(String username, boolean enable);

    UserEntity findByUsername(String username);

}
