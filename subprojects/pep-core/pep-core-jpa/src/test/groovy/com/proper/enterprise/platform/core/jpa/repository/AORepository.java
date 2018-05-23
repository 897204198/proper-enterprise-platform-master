package com.proper.enterprise.platform.core.jpa.repository;

import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.core.jpa.entity.AOEntity;

public interface AORepository extends BaseRepository<AOEntity, String> {

    @CacheQuery
    AOEntity findByUsername(String username);

}
