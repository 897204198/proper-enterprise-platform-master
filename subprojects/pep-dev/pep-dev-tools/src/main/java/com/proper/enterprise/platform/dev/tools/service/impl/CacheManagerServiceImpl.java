package com.proper.enterprise.platform.dev.tools.service.impl;

import com.proper.enterprise.platform.api.cache.CacheKeysSentry;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.dev.tools.service.CacheManagerService;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class CacheManagerServiceImpl implements CacheManagerService {

    @Value("${pep.dev.tools.defaultPageSize}")
    private Integer defaultPageSize;

    @Value("${pep.dev.tools.defaultPageNo}")
    private Integer defaultPageNo;

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CacheKeysSentry sentry;
    @Autowired
    private I18NService i18NService;

    @Override
    public Cache getCacheByName(String cacheName) {
        return cacheManager.getCache(cacheName);
    }

    @Override
    public Collection<String> getAllCacheNames() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        if (cacheNames == null || cacheNames.size() == 0) {
            return null;
        }
        List<String> list = new ArrayList<>(cacheNames);
        Collections.sort(list);
        return list;
    }

    @Override
    public List<String> getCacheKeysByCacheNamePageable(String cacheName, Integer pageNo, Integer pageSize) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            return null;
        }
        Collection collection = sentry.keySet(cache);
        if (collection == null || collection.size() == 0) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (Object object : collection) {
            String keyjson;
            if (object instanceof String) {
                keyjson = object.toString();
            } else {
                keyjson = JSONUtil.toJSONIgnoreException(object);
            }
            list.add(keyjson);
        }
        Collections.sort(list);
        if (pageNo == null && pageSize == null) {
            return list;
        }
        if (pageNo == null || pageNo <= 0) {
            pageNo = defaultPageNo;
        }
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        Integer startIdx = (pageNo - 1) * pageSize;
        Integer endIdx = startIdx + pageSize;
        if (endIdx > list.size()) {
            endIdx = list.size();
        }
        if (startIdx <= endIdx && endIdx <= list.size()) {
            list = list.subList(startIdx, endIdx);
        }
        return list;
    }

    @Override
    public Object getKeyValueByClassName(String cacheName, String key, String className) throws Exception {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            return null;
        }
        Cache.ValueWrapper valueWrapper;
        Object objValue;
        if (StringUtil.isNotNull(className)) {
            try {
                Class name = Class.forName(className);
                objValue = cache.get(key, name);
                return objValue;
            } catch (ClassNotFoundException e) {
                throw new Exception(i18NService.getMessage("pep.admin.biz.rule.value"));
            }
        }
        valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        return valueWrapper.get();
    }

    @Override
    public void deleteByName(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    @Override
    public void deleteByNames(List<String> cacheNames) {
        for (String cacheName : cacheNames) {
            this.deleteByName(cacheName);
        }
    }

    @Override
    public void deleteByKeys(String cacheName, List<String> keys) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            for (String key : keys) {
                if (cache.get(key) != null) {
                    cache.evict(key);
                }
            }
        }
    }
}
