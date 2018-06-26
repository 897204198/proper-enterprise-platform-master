package com.proper.enterprise.platform.core.jpa.repository;

import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.core.jpa.entity.AOEntity;

public interface AORepository extends BaseRepository<AOEntity, String> {

    /**
     * 通过username查询
     * @param username username
     * @return AOEntity实体
     */
    @CacheQuery
    AOEntity findByUsername(String username);

}
