package com.proper.enterprise.platform.auth.repository;

import com.proper.enterprise.platform.auth.entity.UserEntity;
import com.proper.enterprise.platform.core.BaseRepository;
import com.proper.enterprise.platform.core.annotation.CacheQuery;

public interface UserRepository extends BaseRepository<UserEntity, String> {

    @CacheQuery
    UserEntity findByLoginName(String loginName);

}
