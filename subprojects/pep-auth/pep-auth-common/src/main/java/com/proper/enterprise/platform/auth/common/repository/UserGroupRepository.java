package com.proper.enterprise.platform.auth.common.repository;

import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;

import java.util.Collection;

public interface UserGroupRepository extends BaseRepository<UserGroupEntity, String> {

    Collection<UserGroupEntity> findAllByValidTrue();

    UserGroupEntity findByValidAndId(boolean valid, String id);

    UserGroupEntity findByValidAndName(boolean valid, String name);

}
