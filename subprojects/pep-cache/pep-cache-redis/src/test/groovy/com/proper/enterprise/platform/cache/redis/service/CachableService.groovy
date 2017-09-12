package com.proper.enterprise.platform.cache.redis.service

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
@CacheConfig(cacheNames = "test")
class CachableService {

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

}
