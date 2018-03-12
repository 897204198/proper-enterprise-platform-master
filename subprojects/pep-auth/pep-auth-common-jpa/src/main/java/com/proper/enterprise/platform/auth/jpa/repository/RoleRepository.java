package com.proper.enterprise.platform.auth.jpa.repository;

import com.proper.enterprise.platform.auth.jpa.entity.RoleEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;

import java.util.Collection;
import java.util.List;

public interface RoleRepository extends BaseRepository<RoleEntity, String> {

    Collection<RoleEntity> findByNameAndValidAndEnable(String name, boolean valid, boolean enable);

    List<RoleEntity> findAllByValidTrueAndEnableTrue();

    RoleEntity findByIdAndValid(String id, boolean valid);

    Collection<RoleEntity> findAllByNameLike(String name);

}
