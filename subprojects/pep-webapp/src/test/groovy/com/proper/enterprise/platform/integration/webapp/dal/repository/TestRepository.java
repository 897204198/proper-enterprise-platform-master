package com.proper.enterprise.platform.integration.webapp.dal.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.core.annotation.CacheQuery;
import com.proper.enterprise.platform.integration.webapp.dal.entity.TestEntity;

public interface TestRepository extends BaseRepository<TestEntity, String> {

    @CacheQuery
    TestEntity findByLoginName(String loginName);

}
