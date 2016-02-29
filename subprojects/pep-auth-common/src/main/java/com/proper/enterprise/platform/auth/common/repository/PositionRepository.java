package com.proper.enterprise.platform.auth.common.repository;

import com.proper.enterprise.platform.auth.common.entity.PositionEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;

public interface PositionRepository extends BaseRepository<PositionEntity, String> {

    PositionEntity findByName(String name);

}
