package com.proper.enterprise.platform.sequence.handler;

import com.proper.enterprise.platform.api.cache.CacheDuration;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

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
        Optional<Cache.ValueWrapper> valueWrapper = Optional.ofNullable(cache.get(atomicName));
        if (!valueWrapper.isPresent()) {
            cache.putIfAbsent(atomicName, 0L);
        }
        synchronized (SerialNumberHandler.class) {
            valueWrapper = Optional.ofNullable(cache.get(atomicName));
            Long object =  Optional.ofNullable((Long) valueWrapper.get().get()).orElse(0L);
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
        Cache cache = getCache();
        Optional<Cache.ValueWrapper> valueWrapper = Optional.ofNullable(cache.get(atomicName));
        if (!valueWrapper.isPresent()) {
            return 0L;
        } else {
            return Optional.ofNullable((Long) valueWrapper.get().get()).orElse(0L);
        }
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
        Assert.notNull(cacheManager.getCache(CACHE_NAME), I18NUtil.getMessage("sequence.cache.get.fail"));
        return cacheManager.getCache(CACHE_NAME);
    }
}
