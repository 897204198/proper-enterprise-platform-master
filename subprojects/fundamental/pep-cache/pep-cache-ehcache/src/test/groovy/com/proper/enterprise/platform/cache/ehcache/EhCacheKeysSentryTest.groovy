package com.proper.enterprise.platform.cache.ehcache

import com.proper.enterprise.platform.api.cache.CacheKeysSentry
import com.proper.enterprise.platform.cache.ehcache.service.CacheableService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager

class EhCacheKeysSentryTest extends AbstractJPATest {

    @Autowired
    private CacheManager cacheManager
    @Autowired
    private CacheKeysSentry sentry
    @Autowired
    private CacheableService service

    @Before
    void setUp() {
        service.setKey('sentry')
    }

    @After
    void tearDown() {
        service.clearKey()
    }

    @Test
    void test() {
        def cache = cacheManager.getCache('apiSecrets')
        def set = sentry.keySet(cache)
        assert set.size() == 1
        assert cache.get(set.iterator().next()).get() == 'sentry'
    }

}
