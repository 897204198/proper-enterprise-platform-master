package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.UserGroupEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

import java.util.Collection;

public interface UserGroupRepository extends BaseJpaRepository<UserGroupEntity, String> {

    Collection<UserGroupEntity> findAllByValidTrue();

    UserGroupEntity findByValidAndId(boolean valid, String id);

    UserGroupEntity findByValidAndName(boolean valid, String name);

}
