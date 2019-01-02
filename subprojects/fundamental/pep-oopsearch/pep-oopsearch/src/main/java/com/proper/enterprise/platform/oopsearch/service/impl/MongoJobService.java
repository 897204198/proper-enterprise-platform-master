package com.proper.enterprise.platform.oopsearch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.proper.enterprise.platform.api.cache.CacheKeysSentry;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.sync.SingleSync;
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
    private SingleSync singleSync;

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
                    LOGGER.debug("sync data change to mongo pri:{}", syncDocumentModel.getPri());
                    singleSync.singleSynchronization(syncDocumentModel);
                    syncDocumentModel.setProcess(true);
                    try {
                        cache.put(key, JSONUtil.getMapper().writeValueAsString(syncDocumentModel));
                    } catch (JsonProcessingException e) {
                        LOGGER.error(e.getMessage());
                    }
                    LOGGER.debug("sync data change to mongo finish pri:{}", syncDocumentModel.getPri());
                    return;
                }
            }
        }
    }
}
