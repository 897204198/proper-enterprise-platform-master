package com.proper.enterprise.platform.cache.redis

import com.proper.enterprise.platform.cache.redis.service.CacheableService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager

class SpringCacheTest extends AbstractTest  {

    @Autowired
    CacheableService service

    @Autowired
    CacheManager cacheManager

    @Before
    void setUp() {
        service.clear()
    }

    @Test
    void checkRedisCacheWorks() {
        synchronized (SpringCacheTest) {
            4.times {
                assert service.addCount() == 6
            }
        }
        service.clear()
        synchronized (SpringCacheTest) {
            4.times {
                assert service.addCount() == 7
            }
        }
        assert service.newKey() == 10
    }

    @Test
    void notOverWriteDupKeyUnderDifferentCacheNames() {
        service.clearKey()
        service.clearAnotherKey()

        service.setKey('k1')
        service.setKeyInOtherCacheName([])
        assert service.setKey('') == 'k1'
        assert service.setKeyInOtherCacheName('k2') == []
    }

    @Test
    void checkTTL() {
        def ro = cacheManager.getCache('test').getNativeCache()
        synchronized (SpringCacheTest) {
            service.addCount()

            assert ro.get('count') != null
            // -1 means the key exists but has no associated expire.
            assert ro.remainTimeToLive() == -1
        }
        sleep(1000)
        assert ro.get('count') == null
        assert ro.remainTimeToLive() == -1
    }

    @Test
    void testCacheDuration() {
        def t1,t2
        synchronized (SpringCacheTest) {
            t1 = service.testWithCacheName()
            assert t1 == service.testWithCacheName()
        }
        sleep(600)
        assert t1 != service.testWithCacheName()

        synchronized (SpringCacheTest) {
            t2 = service.testWithoutCacheName()
            assert t2 == service.testWithoutCacheName()
        }
        sleep(600)
        assert t2 != service.testWithoutCacheName()

        service.evit()
    }

    @Test
    void testListCache() {
        def list = service.getList()
        list.add('c')
        assert !service.getList().contains('c')
    }

}
