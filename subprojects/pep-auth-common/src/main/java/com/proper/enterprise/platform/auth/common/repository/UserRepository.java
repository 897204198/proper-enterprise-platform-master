package com.proper.enterprise.platform.auth.common.repository;

import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.core.annotation.CacheQuery;

public interface UserRepository extends BaseRepository<UserEntity, String> {

    @CacheQuery
    UserEntity findByLoginName(String loginName);

}
