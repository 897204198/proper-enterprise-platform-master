package com.proper.enterprise.platform.oopsearch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.proper.enterprise.platform.api.cache.CacheDuration;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.oopsearch.config.entity.SearchConfigEntity;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.config.repository.SearchConfigRepository;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheDuration(ttl = 60000L, cacheName = "syncMongo")
public class SyncCacheService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncCacheService.class);

    @Autowired
    CacheManager cacheManager;

    @Autowired
    SearchConfigService searchConfigService;

    @Autowired
    SearchConfigRepository searchConfigRepository;

    public void push(String pos, SyncDocumentModel syncDocumentModel) {
        switch (syncDocumentModel.getMethod()) {
            case INSERT:
            case UPDATE:
                handleInsertOrUpdate(pos, syncDocumentModel);
                break;
            case DELETE:
                handleDelete(pos, syncDocumentModel);
                break;
            default:
                break;
        }
    }

    private void handleInsertOrUpdate(String pos, SyncDocumentModel syncDocumentModel) {
        SearchConfigEntity syncConf = getSyncConf(syncDocumentModel);
        if (null == syncConf) {
            return;
        }
        syncCache(pos, fillDocument(syncDocumentModel, syncConf));
    }

    private void handleDelete(String pos, SyncDocumentModel syncDocumentModel) {
        List<SearchConfigEntity> configEntities = searchConfigRepository.findByTableName(syncDocumentModel.getTab());
        if (null != configEntities && configEntities.size() > 0) {
            syncCache(pos, syncDocumentModel);
        }
    }

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

    private SearchConfigEntity getSyncConf(SyncDocumentModel syncDocumentModel) {
        Map<String, Object> searchConfigBeans = searchConfigService.getSearchConfigs(syncDocumentModel.getDataBaseType());
        if (searchConfigBeans == null) {
            LOGGER.error("search config is null");
            return null;
        }
        //key=tableName+|+colunmName
        Map<String, SearchConfigEntity> configMap = new HashMap<>(16);
        List<SearchConfigEntity> searchConfigEntities = searchConfigRepository.findByDataBaseType(syncDocumentModel.getDataBaseType());
        for (SearchConfigEntity searchConfig : searchConfigEntities) {
            configMap.put(searchConfig.getTableName().toLowerCase() + "|" + searchConfig.getSearchColumn().toLowerCase(), searchConfig);
        }
        return configMap.get(syncDocumentModel.getTab().toLowerCase() + "|" + syncDocumentModel.getCol().toLowerCase());
    }

    private SyncDocumentModel fillDocument(SyncDocumentModel syncDocumentModel, SearchConfigEntity syncConf) {
        syncDocumentModel.setDes(syncConf.getColumnDesc());
        syncDocumentModel.setAlias(syncConf.getColumnAlias());
        syncDocumentModel.setUrl(syncConf.getUrl());
        return syncDocumentModel;
    }

}
