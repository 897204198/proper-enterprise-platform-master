package com.proper.enterprise.platform.cache.redis;

import com.proper.enterprise.platform.cache.CacheDuration;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A {@link org.springframework.cache.CacheManager} implementation
 * backed by Redisson instance.
 *
 * @author Nikita Koksharov
 *
 * Copy from {@link org.redisson.spring.cache.RedissonSpringCacheManager} v3.4.3
 * Extend {@link RedissonSpringCacheManager#afterPropertiesSet()} to support {@link com.proper.enterprise.platform.cache.CacheDuration}
 */
public class RedisCacheManager implements CacheManager, ResourceLoaderAware, InitializingBean {

    private ResourceLoader resourceLoader;

    private boolean dynamic = true;

    private boolean allowNullValues = true;

    private Codec codec;

    private RedissonClient redisson;

    private Map<String, CacheConfig> configMap = new ConcurrentHashMap<String, CacheConfig>();
    private ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<String, Cache>();

    private String configLocation;

    /**
     * Creates CacheManager supplied by Redisson instance
     *
     * @param redisson object
     */
    public RedisCacheManager(RedissonClient redisson) {
        this(redisson, (String)null, null);
    }

    /**
     * Creates CacheManager supplied by Redisson instance and
     * Cache config mapped by Cache name
     *
     * @param redisson object
     * @param config object
     */
    public RedisCacheManager(RedissonClient redisson, Map<String, CacheConfig> config) {
        this(redisson, config, null);
    }

    /**
     * Creates CacheManager supplied by Redisson instance, Codec instance
     * and Cache config mapped by Cache name.
     * <p>
     * Each Cache instance share one Codec instance.
     *
     * @param redisson object
     * @param config object
     * @param codec object
     */
    public RedisCacheManager(RedissonClient redisson, Map<String, CacheConfig> config, Codec codec) {
        this.redisson = redisson;
        this.configMap = config;
        this.codec = codec;
    }

    /**
     * Creates CacheManager supplied by Redisson instance
     * and Cache config mapped by Cache name.
     * <p>
     * Loads the config file from the class path, interpreting plain paths as class path resource names
     * that include the package path (e.g. "mypackage/myresource.txt").
     *
     * @param redisson object
     * @param configLocation path
     */
    public RedisCacheManager(RedissonClient redisson, String configLocation) {
        this(redisson, configLocation, null);
    }

    /**
     * Creates CacheManager supplied by Redisson instance, Codec instance
     * and Config location path.
     * <p>
     * Each Cache instance share one Codec instance.
     * <p>
     * Loads the config file from the class path, interpreting plain paths as class path resource names
     * that include the package path (e.g. "mypackage/myresource.txt").
     *
     * @param redisson object
     * @param configLocation path
     * @param codec object
     */
    public RedisCacheManager(RedissonClient redisson, String configLocation, Codec codec) {
        this.redisson = redisson;
        this.configLocation = configLocation;
        this.codec = codec;
    }

    /**
     * Defines possibility of storing {@code null} values.
     * <p>
     * Default is <code>true</code>
     *
     * @param allowNullValues - stores if <code>true</code>
     */
    public void setAllowNullValues(boolean allowNullValues) {
        this.allowNullValues = allowNullValues;
    }

    /**
     * Defines 'fixed' cache names.
     * A new cache instance will not be created in dynamic for non-defined names.
     * <p>
     * `null` parameter setups dynamic mode
     *
     * @param names of caches
     */
    public void setCacheNames(Collection<String> names) {
        if (names != null) {
            for (String name : names) {
                getCache(name);
            }
            dynamic = false;
        } else {
            dynamic = true;
        }
    }

    /**
     * Set cache config location
     *
     * @param configLocation object
     */
    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * Set cache config mapped by cache name
     *
     * @param config object
     */
    public void setConfig(Map<String, CacheConfig> config) {
        this.configMap = config;
    }

    /**
     * Set Redisson instance
     *
     * @param redisson instance
     */
    public void setRedisson(RedissonClient redisson) {
        this.redisson = redisson;
    }

    /**
     * Set Codec instance shared between all Cache instances
     *
     * @param codec object
     */
    public void setCodec(Codec codec) {
        this.codec = codec;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = instanceMap.get(name);
        if (cache != null) {
            return cache;
        }
        if (!dynamic) {
            return cache;
        }

        CacheConfig config = configMap.get(name);
        if (config == null) {
            config = new CacheConfig();
            configMap.put(name, config);

            return createMap(name);
        }

        if (config.getMaxIdleTime() == 0 && config.getTTL() == 0) {
            return createMap(name);
        }

        return createMapCache(name, config);
    }

    private Cache createMap(String name) {
        RMap<Object, Object> map;
        if (codec != null) {
            map = redisson.getMap(name, codec);
        } else {
            map = redisson.getMap(name);
        }

        Cache cache = new RedissonCache(map, allowNullValues);
        Cache oldCache = instanceMap.putIfAbsent(name, cache);
        if (oldCache != null) {
            cache = oldCache;
        }
        return cache;
    }

    private Cache createMapCache(String name, CacheConfig config) {
        RMapCache<Object, Object> map;
        if (codec != null) {
            map = redisson.getMapCache(name, codec);
        } else {
            map = redisson.getMapCache(name);
        }

        Cache cache = new RedissonCache(map, config, allowNullValues);
        Cache oldCache = instanceMap.putIfAbsent(name, cache);
        if (oldCache != null) {
            cache = oldCache;
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(configMap.keySet());
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    // Different form here

    @Override
    public void afterPropertiesSet() throws Exception {
        if (configLocation == null) {
            return;
        }

        Resource resource = resourceLoader.getResource(configLocation);
        try {
            // try to read yaml first
            this.configMap = CacheConfig.fromYAML(resource.getInputStream());
        } catch (IOException e) {
            try {
                this.configMap = CacheConfig.fromJSON(resource.getInputStream());
            } catch (IOException e1) {
                throw new BeanDefinitionStoreException(
                    "Could not parse cache configuration at [" + configLocation + "]", e1);
            }
        }
        this.configMap.putAll(configsFormCacheDuration());
    }

    private Map<String, CacheConfig> configsFormCacheDuration() {
        Map<String, CacheConfig> result = new HashMap<>();
        Reflections reflections = new Reflections("com.proper", new MethodAnnotationsScanner());
        Set<Method> namedMethods = reflections.getMethodsAnnotatedWith(CacheDuration.class);
        CacheDuration cd;
        String cacheName;
        for (Method method : namedMethods) {
            cd = method.getAnnotation(CacheDuration.class);
            cacheName = StringUtils.hasText(cd.cacheName()) ? cd.cacheName() : method.getDeclaringClass().getCanonicalName() + "#" + method.getName();
            result.put(cacheName, new CacheConfig(cd.ttl(), cd.maxIdleTime()));
        }
        return result;
    }

}
