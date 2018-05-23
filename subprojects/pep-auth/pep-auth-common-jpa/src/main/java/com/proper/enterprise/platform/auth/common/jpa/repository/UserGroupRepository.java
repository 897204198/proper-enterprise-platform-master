package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.UserGroupEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

public interface UserGroupRepository extends BaseJpaRepository<UserGroupEntity, String> {

    UserGroupEntity findByName(String name);

    UserGroupEntity findByNameAndEnable(String name, boolean enable);

}
