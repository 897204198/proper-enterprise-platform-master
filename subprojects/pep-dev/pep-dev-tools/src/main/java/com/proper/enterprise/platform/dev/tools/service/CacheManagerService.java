package com.proper.enterprise.platform.dev.tools.service;

import org.springframework.cache.Cache;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface CacheManagerService {

    /**
     * 根据缓存名字，获取对应的缓存
     * @param cacheName 缓存名称
     * @return 缓存
     */
    Cache getCacheByName(String cacheName);

    /**
     * 获取所有的缓存的名字
     * @return 缓存的集合
     */
    Collection<String> getAllCacheNames();

    /**
     * 根据缓存的名字,分页查找key值，并安装字典序排列，最后再分页返回
     * 如果pageNo、pageSize 都为空则返回全部,其中一个为空则设成默认
     * @param cacheName 缓存名字
     * @param pageNo    开始页数 默认1
     * @param pageSize  每页显示数量，默认10
     * @return 返回符合条件的key值列表
     * @throws IOException 异常信息
     */
    List<String> getCacheKeysByCacheNamePageable(String cacheName, Integer pageNo, Integer pageSize) throws IOException;

    /**
     * 根据缓存名字，key值，key对应的类型名，获取对应的value值
     * @param cacheName 缓存的名称
     * @param key 缓存的key
     * @param className 类名
     * @return 对象
     * @throws Exception 异常信息
     */
    Object getKeyValueByClassName(String cacheName, String key, String className) throws Exception;

    /**
     * 通过缓存名称删除缓存
     * @param cacheName 缓存的名称
     */
    void deleteByName(String cacheName);

    /**
     * 根据名字集合删除缓存
     * @param cacheNames 缓存名称的集合
     */
    void deleteByNames(List<String> cacheNames);

    /**
     * 根据缓存名字、里面的key集合，删除对应的key
     * @param cacheName 缓存名称
     * @param keys 缓存的key
     */
    void deleteByKeys(String cacheName, List<String> keys);

}
