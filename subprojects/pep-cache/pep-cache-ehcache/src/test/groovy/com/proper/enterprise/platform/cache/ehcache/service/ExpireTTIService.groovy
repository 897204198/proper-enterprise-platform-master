package com.proper.enterprise.platform.cache.ehcache.service

import com.proper.enterprise.platform.cache.CacheDuration
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
@CacheConfig(cacheNames = 'PEP.TEST.expireTTI')
@CacheDuration(cacheName = 'PEP.TEST.expireTTI', maxIdleTime = 1000L)
class ExpireTTIService {

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
