package com.proper.enterprise.platform.integration.webapp.cache

import com.proper.enterprise.platform.integration.webapp.dal.entity.TestEntity
import com.proper.enterprise.platform.integration.webapp.dal.repository.TestRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import net.sf.ehcache.Cache
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
        Cache sqc = cacheManager.getCache('org.hibernate.cache.internal.StandardQueryCache').nativeCache
        Cache utc = cacheManager.getCache('org.hibernate.cache.spi.UpdateTimestampsCache').nativeCache
        assert sqc.size == 0
        assert utc.size == 0

        repo.findByLoginName('abc')
        assert sqc.size == 1
        assert utc.size == 1

        def sqcHitcount = sqc.get(sqc.keys[0]).hitCount
        def utcHitcount = utc.get(utc.keys[0]).hitCount

        3.times {
            repo.findByLoginName('abc')
            def t1 = sqc.get(sqc.keys[0]).hitCount
            def t2 = utc.get(utc.keys[0]).hitCount
            assert t1 == sqcHitcount + 2
            assert t2 == utcHitcount + 2
            sqcHitcount = t1
            utcHitcount = t2
        }

    }

}
