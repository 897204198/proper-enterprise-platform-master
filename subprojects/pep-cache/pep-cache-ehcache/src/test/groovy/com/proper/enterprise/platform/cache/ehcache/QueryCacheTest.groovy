package com.proper.enterprise.platform.cache.ehcache

import com.proper.enterprise.platform.cache.ehcache.entity.AnEntity
import com.proper.enterprise.platform.cache.ehcache.repository.AnRepository
import com.proper.enterprise.platform.test.AbstractTest
import net.sf.ehcache.Cache
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager

class QueryCacheTest extends AbstractTest {

    @Autowired
    CacheManager cacheManager

    @Autowired
    AnRepository repo

    @Before
    void setUp() {
        repo.save(new AnEntity('abc', '123'))
    }

    @Test
    void checkQueryCache() {
        // query cache and second level cache use these two cache keys
        Cache sqc = cacheManager.getCache('org.hibernate.cache.internal.StandardQueryCache').nativeCache
        Cache utc = cacheManager.getCache('org.hibernate.cache.spi.UpdateTimestampsCache').nativeCache
        sqc.removeAll()
        utc.removeAll()
        // non cache element in these caches
        assert sqc.size == 0
        assert utc.size == 0

        AnEntity entity = repo.findByUsername('abc')
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
