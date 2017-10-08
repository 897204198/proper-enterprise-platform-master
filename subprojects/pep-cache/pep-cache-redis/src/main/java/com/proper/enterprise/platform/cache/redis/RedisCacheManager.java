package com.proper.enterprise.platform.cache.redis;

import com.proper.enterprise.platform.api.cache.CacheDuration;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Copy from {@link org.redisson.spring.cache.RedissonSpringCacheManager} v3.5.3
 * Extend {@link RedissonSpringCacheManager#afterPropertiesSet()} to support {@link com.proper.enterprise.platform.cache.CacheDuration}
 */
public class RedisCacheManager implements CacheManager, ResourceLoaderAware, InitializingBean {

    ResourceLoader resourceLoader;

    private boolean dynamic = true;

    private boolean allowNullValues = true;

    Codec codec;

    RedissonClient redisson;

    Map<String, CacheConfig> configMap = new ConcurrentHashMap<String, CacheConfig>();
    ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<String, Cache>();

    String configLocation;

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
    public RedisCacheManager(RedissonClient redisson, Map<String, ? extends CacheConfig> config) {
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
    public RedisCacheManager(RedissonClient redisson, Map<String, ? extends CacheConfig> config, Codec codec) {
        this.redisson = redisson;
        this.configMap = (Map<String, CacheConfig>) config;
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
    public void setConfig(Map<String, ? extends CacheConfig> config) {
        this.configMap = (Map<String, CacheConfig>) config;
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

    protected CacheConfig createDefaultConfig() {
        return new CacheConfig();
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
            config = createDefaultConfig();
            configMap.put(name, config);

            return createMap(name, config);
        }

        if (config.getMaxIdleTime() == 0 && config.getTTL() == 0) {
            return createMap(name, config);
        }

        return createMapCache(name, config);
    }

    private Cache createMap(String name, CacheConfig config) {
        RMap<Object, Object> map = getMap(name, config);

        Cache cache = new RedissonCache(map, allowNullValues);
        Cache oldCache = instanceMap.putIfAbsent(name, cache);
        if (oldCache != null) {
            cache = oldCache;
        }
        return cache;
    }

    protected RMap<Object, Object> getMap(String name, CacheConfig config) {
        if (codec != null) {
            return redisson.getMap(name, codec);
        }
        return redisson.getMap(name);
    }

    private Cache createMapCache(String name, CacheConfig config) {
        RMapCache<Object, Object> map = getMapCache(name, config);

        Cache cache = new RedissonCache(map, config, allowNullValues);
        Cache oldCache = instanceMap.putIfAbsent(name, cache);
        if (oldCache != null) {
            cache = oldCache;
        }
        return cache;
    }

    protected RMapCache<Object, Object> getMapCache(String name, CacheConfig config) {
        if (codec != null) {
            return redisson.getMapCache(name, codec);
        }
        return redisson.getMapCache(name);
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
            this.configMap = (Map<String, CacheConfig>) CacheConfig.fromYAML(resource.getInputStream());
        } catch (IOException e) {
            try {
                this.configMap = (Map<String, CacheConfig>) CacheConfig.fromJSON(resource.getInputStream());
            } catch (IOException e1) {
                throw new BeanDefinitionStoreException(
                    "Could not parse cache configuration at [" + configLocation + "]", e1);
            }
        }
        this.configMap.putAll(configsFormCacheDuration());
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheManager.class);

    private Map<String, CacheConfig> configsFormCacheDuration() {
        LOGGER.info("Start to load configs from @CacheDuration");
        CacheDuration cd;
        String cacheName;

        Set<Class<?>> cdTypes = new Reflections("com.proper").getTypesAnnotatedWith(CacheDuration.class);
        Set<Method> cdMethods = new Reflections("com.proper", new MethodAnnotationsScanner()).getMethodsAnnotatedWith(CacheDuration.class);

        Map<String, CacheConfig> result = new HashMap<>(cdTypes.size() + cdMethods.size());
        for (Class clz : cdTypes) {
            cd = (CacheDuration) clz.getAnnotation(CacheDuration.class);
            if (cd == null) {
                LOGGER.debug("Could NOT find CacheDuration on {}, it maybe on its super class.", clz.getCanonicalName());
                continue;
            }
            cacheName = StringUtils.hasText(cd.cacheName()) ? cd.cacheName() : clz.getCanonicalName();
            LOGGER.debug("Load {} with {} ttl and {} max idle time.", cacheName, cd.ttl(), cd.maxIdleTime());
            result.put(cacheName, new CacheConfig(cd.ttl(), cd.maxIdleTime()));
        }
        // Method annotation has higher priority with same cache name
        String canonicalName;
        for (Method method : cdMethods) {
            cd = method.getAnnotation(CacheDuration.class);
            canonicalName = method.getDeclaringClass().getCanonicalName() + "#" + method.getName();
            if (cd == null) {
                LOGGER.debug("Could NOT find CacheDuration on {}", canonicalName);
                continue;
            }
            cacheName = StringUtils.hasText(cd.cacheName()) ? cd.cacheName() : canonicalName;
            LOGGER.debug("Load {} with {}ms ttl and {}ms max idle time.", cacheName, cd.ttl(), cd.maxIdleTime());
            result.put(cacheName, new CacheConfig(cd.ttl(), cd.maxIdleTime()));
        }
        return result;
    }

}
