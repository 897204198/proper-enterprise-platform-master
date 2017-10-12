package com.proper.enterprise.platform.api.cache;

import org.springframework.cache.Cache;

import java.util.Collection;

/**
 * 缓存 key 哨兵
 * 用来列出缓存区域下的所有 key
 */
public interface CacheKeysSentry {

    /**
     * 从缓存中获取所有 key 的集合
     * 依赖具体的缓存实现
     *
     * @param  cache 缓存区域
     * @return key 集合
     */
    Collection<Object> keySet(Cache cache);

}
