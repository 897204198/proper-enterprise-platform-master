package com.proper.enterprise.platform.integration.webapp.cache

import com.proper.enterprise.platform.integration.webapp.dal.entity.AEntity
import com.proper.enterprise.platform.integration.webapp.dal.repository.ARepository
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
    ARepository repo

    @Before
    public void setUp() {
        repo.save(new AEntity('abc', '123'))
    }

    @Test
    public void checkQueryCache() {
        // query cache and second level cache use these two cache keys
        Cache sqc = cacheManager.getCache('org.hibernate.cache.internal.StandardQueryCache').nativeCache
        Cache utc = cacheManager.getCache('org.hibernate.cache.spi.UpdateTimestampsCache').nativeCache
        sqc.removeAll()
        utc.removeAll()
        // non cache element in these caches
        assert sqc.size == 0
        assert utc.size == 0

        AEntity entity = repo.findByUsername('abc')
        // cache entity after first load
        assert sqc.size == 1
        assert utc.size == 1

        def eleInSqc = sqc.get(sqc.keys[0])
        def eleInUtc = utc.get(utc.keys[0])
        def sqcHitcount = eleInSqc.hitCount
        def utcHitcount = eleInUtc.hitCount

        // hit count of cache will be increased after each load operation
        3.times {
            repo.findByUsername('abc')
            def t1 = eleInSqc.hitCount
            def t2 = eleInUtc.hitCount
            assert t1 == sqcHitcount + 1
            assert t2 == utcHitcount + 1
            sqcHitcount = t1
            utcHitcount = t2
        }

        entity.setDescription('update_account')
        repo.save(entity)

        repo.findByUsername('abc')
        // hit count will be reset after update
        assert sqc.get(sqc.keys[0]).hitCount < sqcHitcount
        assert utc.get(utc.keys[0]).hitCount < utcHitcount
    }

}
