package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

public interface ResourceRepository extends BaseJpaRepository<ResourceEntity, String> {

    ResourceEntity findByIdAndEnable(String id, boolean enable);
}
