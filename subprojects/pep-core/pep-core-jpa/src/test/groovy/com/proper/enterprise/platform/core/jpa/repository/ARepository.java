package com.proper.enterprise.platform.core.jpa.repository;

import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.core.jpa.entity.AEntity;

public interface ARepository extends BaseRepository<AEntity, String> {

    @CacheQuery
    AEntity findByUsername(String username);

}
