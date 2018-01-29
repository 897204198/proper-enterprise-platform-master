package com.proper.enterprise.platform.auth.common.repository;

import com.proper.enterprise.platform.auth.common.entity.RoleEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;

import java.util.Collection;

public interface RoleRepository extends BaseRepository<RoleEntity, String> {

    Collection<RoleEntity> findByName(String name);

    Collection<RoleEntity> findAllByNameLike(String name);

}
