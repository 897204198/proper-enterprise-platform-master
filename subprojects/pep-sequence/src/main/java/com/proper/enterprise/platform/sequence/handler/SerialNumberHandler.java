package com.proper.enterprise.platform.sequence.handler;

import com.proper.enterprise.platform.api.cache.CacheDuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@CacheDuration(cacheName = "pep.cache.serialNumberHandler")
public class SerialNumberHandler {

    private static final String CACHE_NAME = "pep.cache.serialNumberHandler";

    private CacheManager cacheManager;

    @Autowired
    public SerialNumberHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 获取cache中自增值
     *
     * @param atomicName 缓存key
     * @return 自增值
     */
    public Long getNextID(String atomicName) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        Cache.ValueWrapper valueWrapper = cache.get(atomicName);
        if (valueWrapper == null) {
            cache.putIfAbsent(atomicName, 0L);
        }
        synchronized (SerialNumberHandler.class) {
            Long object = (Long)cache.get(atomicName).get();
            cache.put(atomicName, object + 1);
            return object + 1;
        }
    }

    /**
     * 获取cache中当前值
     *
     * @param atomicName 缓存key
     * @return 当前值
     */
    public Long getCurrentID(String atomicName) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        return cache.get(atomicName) == null ? 0L : Long.parseLong(cache.get(atomicName).get().toString());
    }

    /**
     * 更新cache中的当前值
     *
     * @param atomicName 缓存key
     * @param newValue 更新值
     */
    public void setCurrentId(String atomicName, Long newValue) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        cache.put(atomicName, newValue);
    }
}
