package com.proper.enterprise.platform.sequence.handler;

import com.proper.enterprise.platform.api.cache.CacheDuration;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
        Cache cache = getCache();
        synchronized (SerialNumberHandler.class) {
            Long currentId = getCurrentID(atomicName);
            cache.put(atomicName, currentId + 1);
            return currentId + 1;
        }
    }

    /**
     * 获取cache中当前值
     *
     * @param atomicName 缓存key
     * @return 当前值
     */
    public Long getCurrentID(String atomicName) {
        Cache cache = getCache();
        Cache.ValueWrapper valueWrapper = cache.get(atomicName);
        if (valueWrapper == null) {
            cache.putIfAbsent(atomicName, 0L);
            return 0L;
        }
        return (Long) valueWrapper.get();
    }

    /**
     * 更新cache中的当前值
     *
     * @param atomicName 缓存key
     * @param newValue 更新值
     */
    public void setCurrentId(String atomicName, Long newValue) {
        Cache cache = getCache();
        cache.put(atomicName, newValue);
    }

    /**
     * 获取流水号的缓存
     *
     * @return Cache
     */
    private Cache getCache() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        Assert.notNull(cache, I18NUtil.getMessage("sequence.cache.get.fail"));
        return cache;
    }
}
