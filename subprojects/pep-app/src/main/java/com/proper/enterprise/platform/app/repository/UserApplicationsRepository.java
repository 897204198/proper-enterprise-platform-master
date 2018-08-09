package com.proper.enterprise.platform.app.repository;

import com.proper.enterprise.platform.app.entity.UserApplicationEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

public interface UserApplicationsRepository extends BaseJpaRepository<UserApplicationEntity, String> {

    /**
     * 获取用户id
     * @param currentUserId 用户id
     * @return 用户与应用的document
     */
    UserApplicationEntity getByUserId(String currentUserId);

}
