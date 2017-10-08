package com.proper.enterprise.platform.cache.ehcache.service

import com.proper.enterprise.platform.api.cache.CacheDuration
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
@CacheConfig(cacheNames = 'PEP.TEST.expireTTL')
@CacheDuration(cacheName = 'PEP.TEST.expireTTL', ttl = 1000L)
class ExpireTTLService {

    int count = 0

    @Cacheable
    int access() {
        ++ count
    }

    @CacheEvict
    void evict() {
        count = 0
    }

}
