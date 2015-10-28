package com.proper.enterprise.platform.integration.auth.spring

import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import net.sf.ehcache.Status
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.security.core.userdetails.UserDetailsService

class CachingUserDetailsIntegTest extends AbstractIntegTest {

    @Autowired
    CacheManager cacheManager

    @Autowired
    UserDetailsService cachingUserDetailsService

    @Test
    public void checkUserCacheStatus() {
        assert cacheManager != null
        assert cacheManager.getCacheNames().contains('userCache')
        Cache cache = cacheManager.getCache('userCache')
        Object nativeCache = cache.getNativeCache()
        assert nativeCache.getClass().getName() == 'net.sf.ehcache.Cache'
        net.sf.ehcache.Cache ehcache = (net.sf.ehcache.Cache) nativeCache
        assert ehcache.status == Status.STATUS_ALIVE
    }

    @Test
    public void checkUserDetailsCache() {
        net.sf.ehcache.Cache ehcache = (net.sf.ehcache.Cache) cacheManager.getCache('userCache').getNativeCache()
        assert ehcache.size == 0

        cachingUserDetailsService.loadUserByUsername('admin')
        assert ehcache.size == 1
        def userCache1 = ehcache.getQuiet('admin').clone()
        assert userCache1 != null

        cachingUserDetailsService.loadUserByUsername('admin')
        assert ehcache.size == 1
        def userCache2 = ehcache.getQuiet('admin').clone()
        assert userCache2 != null

        // here want to check the second query was hit the cache again, but maybe not the property way
        assert (userCache1.creationTime != userCache2.creationTime
                || userCache1.hitCount != userCache2.hitCount
                || userCache1.lastAccessTime != userCache2.lastAccessTime)
    }

}
