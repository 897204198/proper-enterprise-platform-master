package com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.proper.enterprise.platform.api.cache.CacheDuration;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheDuration(ttl = 60000L, cacheName = "syncMongo")
public class SyncCacheService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncCacheService.class);

    @Autowired
    CacheManager cacheManager;

    @Cacheable(cacheNames = "syncMongo", key = "#pos", sync = true)
    public String syncCache(String pos, SyncDocumentModel syncDocumentModel) {
        Cache cache = cacheManager.getCache("syncMongo");
        String value = "";
        try {
            value = JSONUtil.getMapper().writeValueAsString(syncDocumentModel);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
        cache.put(pos, value);
        return value;
    }
}
