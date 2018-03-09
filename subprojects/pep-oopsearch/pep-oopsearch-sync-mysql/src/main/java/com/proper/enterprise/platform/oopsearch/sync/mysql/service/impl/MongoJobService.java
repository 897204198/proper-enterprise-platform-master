package com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.proper.enterprise.platform.api.cache.CacheKeysSentry;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Service("syncMongo")
public class MongoJobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoJobService.class);

    @Autowired
    CacheManager cacheManager;

    @Autowired
    private MongoDataSyncService mongoDataSyncService;

    @Autowired
    private CacheKeysSentry sentry;

    public void syncMongoInsert() {
        Cache cache = cacheManager.getCache("syncMongo");
        Collection<Object> collection = sentry.keySet(cache);
        if (collection.size() > 0) {
            Iterator<Object> iterator = collection.iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                Cache.ValueWrapper valueWrapper = cache.get(key);
                if (valueWrapper == null) {
                    return;
                }
                Object value = valueWrapper.get();
                if (value == null) {
                    return;
                }
                SyncDocumentModel syncDocumentModel;
                try {
                    syncDocumentModel = JSONUtil.getMapper().readValue(value.toString(), SyncDocumentModel.class);
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                    return;
                }
                if (!syncDocumentModel.isProcess()) {
                    String[] arr = key.split(":");
                    String method = "";
                    if (arr != null && arr.length > 1) {
                        method = arr[arr.length - 1].trim();
                    }
                    mongoDataSyncService.singleSynchronization(syncDocumentModel, method);
                    syncDocumentModel.setProcess(true);
                    try {
                        cache.put(key, JSONUtil.getMapper().writeValueAsString(syncDocumentModel));
                    } catch (JsonProcessingException e) {
                        LOGGER.error(e.getMessage());
                    }
                    return;
                }
            }
        }
    }
}
