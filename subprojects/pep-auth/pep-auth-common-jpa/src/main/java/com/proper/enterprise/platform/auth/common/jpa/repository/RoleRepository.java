package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

import java.util.Collection;

public interface RoleRepository extends BaseJpaRepository<RoleEntity, String> {

    Collection<RoleEntity> findByNameAndEnable(String name, boolean enable);
}
