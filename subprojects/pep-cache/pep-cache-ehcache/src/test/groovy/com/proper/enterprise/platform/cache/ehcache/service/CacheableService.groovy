package com.proper.enterprise.platform.cache.ehcache.service

import com.proper.enterprise.platform.cache.CacheDuration
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service('ehCacheableService')
@CacheDuration(ttl = 100L, maxIdleTime = 100L, cacheName = 'com.proper.enterprise.platform.cache.ehcache.service.CacheableService#testWithoutCacheName')
class CacheableService {

    @Cacheable(cacheNames = 'testCacheDuration')
    def testWithCacheName() {
        System.nanoTime()
    }

    @CacheEvict(cacheNames = 'testCacheDuration')
    @CacheDuration(ttl = 1000L, maxIdleTime = 1000L, cacheName = 'testCacheDuration')
    def evit() {

    }

    @CacheDuration(ttl = 1000L, maxIdleTime = 1000L)
    @Cacheable(cacheNames = 'com.proper.enterprise.platform.cache.ehcache.service.CacheableService#testWithoutCacheName')
    def testWithoutCacheName() {
        System.nanoTime()
    }

    @Cacheable(cacheNames = 'listCache')
    @CacheDuration(cacheName = 'listCache')
    def getList() {
        ['a', 'b']
    }

}
