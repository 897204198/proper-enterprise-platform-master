package com.proper.enterprise.platform.cache.ehcache.service

import com.proper.enterprise.platform.cache.CacheDuration
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service('ehCacheableService')
class CacheableService {

    @Cacheable(cacheNames = 'testCacheDuration')
    @CacheDuration(ttl = 1000L, maxIdleTime = 1000L, cacheName = 'testCacheDuration')
    def testWithCacheName() {
        System.nanoTime()
    }

    @Cacheable(cacheNames = 'com.proper.enterprise.platform.cache.ehcache.service.CacheableService#testWithoutCacheName')
    @CacheDuration(ttl = 1000L, maxIdleTime = 1000L)
    def testWithoutCacheName() {
        System.nanoTime()
    }

}
