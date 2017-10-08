package com.proper.enterprise.platform.cache.redis.service

import com.proper.enterprise.platform.api.cache.CacheDuration
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
@CacheConfig(cacheNames = "test")
@CacheDuration(ttl = 10L, maxIdleTime = 10L, cacheName = 'com.proper.enterprise.platform.cache.redis.service.CacheableService#testWithoutCacheName')
class CacheableService {

    private int count = 5

    @Cacheable(key = "'count'")
    int addCount() {
        ++count
    }

    @CacheEvict(key = "'count'")
    void clear() { }

    @CachePut(key = "'count'")
    int newKey() {
        10
    }

    @Cacheable(cacheNames = "apiSecrets", key = "'dupKey'")
    String setKey(String key) {
        key
    }

    @CacheEvict(cacheNames = "apiSecrets", key = "'dupKey'")
    void clearKey() {

    }

    @Cacheable(cacheNames = "anotherCacheName", key = "'dupKey'")
    def setKeyInOtherCacheName(key) {
        key
    }

    @CacheEvict(cacheNames = "anotherCacheName", key = "'dupKey'")
    void clearAnotherKey() {

    }

    @Cacheable(cacheNames = 'testCacheDuration')
    @CacheDuration(ttl = 100L, maxIdleTime = 200L, cacheName = 'testCacheDuration')
    def testWithCacheName() {
        System.nanoTime()
    }

    @CacheEvict(cacheNames = 'testCacheDuration')
    @CacheDuration(ttl = 100L, maxIdleTime = 200L, cacheName = 'testCacheDuration')
    def evit() {

    }

    @CacheDuration(ttl = 100L, maxIdleTime = 200L)
    @Cacheable(cacheNames = 'com.proper.enterprise.platform.cache.redis.service.CacheableService#testWithoutCacheName')
    def testWithoutCacheName() {
        System.nanoTime()
    }

    @Cacheable(cacheNames = 'listCache')
    @CacheDuration(cacheName = 'listCache')
    def getList() {
        ['a', 'b']
    }

}
