package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

import java.util.Collection;
import java.util.List;

public interface RoleRepository extends BaseJpaRepository<RoleEntity, String> {

    Collection<RoleEntity> findByNameAndValidAndEnable(String name, boolean valid, boolean enable);

    List<RoleEntity> findAllByValidTrueAndEnableTrue();

    RoleEntity findByIdAndValid(String id, boolean valid);

    RoleEntity findByIdAndValidAndEnable(String id, boolean valid, boolean enable);

    Collection<RoleEntity> findAllByNameLike(String name);

}
