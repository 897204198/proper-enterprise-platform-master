package com.proper.enterprise.platform.cache.redis.service

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
@CacheConfig(cacheNames = "test")
class CachableService {

    private int count = 5;

    @Cacheable(key = "'count'")
    public int addCount() {
        return ++count;
    }

    @CacheEvict(key = "'count'")
    public void clear() { }

    @CachePut(key = "'count'")
    public int newKey() {
        return 10;
    }

    @Cacheable(cacheNames = "apiSecrets")
    public String setKey(String key) {
        return key;
    }

}
