package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

import java.util.Collection;

public interface RoleRepository extends BaseJpaRepository<RoleEntity, String> {

    /**
     * 通过名称获取角色
     * @param name 名称
     * @param enable enable
     * @return 角色集合
     */
    Collection<RoleEntity> findByNameAndEnable(String name, boolean enable);
}
