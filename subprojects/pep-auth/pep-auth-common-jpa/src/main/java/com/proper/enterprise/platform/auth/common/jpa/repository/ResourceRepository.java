package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

public interface ResourceRepository extends BaseJpaRepository<ResourceEntity, String> {
    ResourceEntity findByValidTrueAndId(String id);

    ResourceEntity findByIdAndValidAndEnable(String id, boolean valid, boolean enable);
}
