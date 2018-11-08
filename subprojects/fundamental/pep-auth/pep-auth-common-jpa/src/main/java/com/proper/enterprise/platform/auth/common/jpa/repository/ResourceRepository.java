package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

import java.util.Collection;

public interface ResourceRepository extends BaseJpaRepository<ResourceEntity, String> {

    /**
     * 查询所有
     * @param enable enable
     * @return 资源信息集合
     */
    Collection<ResourceEntity> findAllByEnable(boolean enable);
}
