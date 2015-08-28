package com.proper.enterprise.platform.integration.webapp.cache

import com.proper.enterprise.platform.integration.webapp.dal.entity.TestEntity
import com.proper.enterprise.platform.integration.webapp.dal.repository.TestRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager

class QueryCacheIntegTest extends AbstractIntegTest {

    @Autowired
    CacheManager cacheManager

    @Autowired
    TestRepository repo

    @Before
    public void setUp() {
        repo.save(new TestEntity('abc', '123'))
    }

    @Test
    public void repeatQuery() {
        println cacheManager.cacheNames
        repo.findByLoginName('abc')
        repo.findByLoginName('abc')
        repo.findByLoginName('abc')
    }

}
