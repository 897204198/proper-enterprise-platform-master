package com.proper.enterprise.platform.integration.webapp.dal.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.core.annotation.CacheQuery;
import com.proper.enterprise.platform.integration.webapp.dal.entity.AEntity;

public interface ARepository extends BaseRepository<AEntity, String> {

    @CacheQuery
    AEntity findByUsername(String username);

}
