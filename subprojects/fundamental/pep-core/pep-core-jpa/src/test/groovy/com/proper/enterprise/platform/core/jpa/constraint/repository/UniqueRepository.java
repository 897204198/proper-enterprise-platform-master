package com.proper.enterprise.platform.core.jpa.constraint.repository;

import com.proper.enterprise.platform.core.jpa.constraint.entity.UniqueEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

public interface UniqueRepository extends BaseJpaRepository<UniqueEntity, String> {
}
