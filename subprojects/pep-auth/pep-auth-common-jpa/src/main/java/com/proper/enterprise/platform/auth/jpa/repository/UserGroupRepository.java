package com.proper.enterprise.platform.auth.jpa.repository;

import com.proper.enterprise.platform.auth.jpa.entity.UserGroupEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;

import java.util.Collection;

public interface UserGroupRepository extends BaseRepository<UserGroupEntity, String> {

    Collection<UserGroupEntity> findAllByValidTrue();

    UserGroupEntity findByValidAndId(boolean valid, String id);

    UserGroupEntity findByValidAndName(boolean valid, String name);
}
