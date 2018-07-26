package com.proper.enterprise.platform.cache.ehcache;

import com.proper.enterprise.platform.api.cache.CacheDuration;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * {@link FactoryBean} that exposes an EhCache {@link net.sf.ehcache.CacheManager}
 * instance (independent or shared), configured from a specified config location.
 *
 * <p>If no config location is specified, a CacheManager will be configured from
 * "ehcache.xml" in the root of the class path (that is, default EhCache initialization
 * - as defined in the EhCache docs - will apply).
 *
 * <p>Setting up a separate EhCacheManagerFactoryBean is also advisable when using
 * EhCacheFactoryBean, as it provides a (by default) independent CacheManager instance
 * and cares for proper shutdown of the CacheManager. EhCacheManagerFactoryBean is
 * also necessary for loading EhCache configuration from a non-default config location.
 *
 * <p>Note: As of Spring 5.0, Spring's EhCache support requires EhCache 2.10 or higher.
 *
 * Copy from {@link org.springframework.cache.ehcache.EhCacheManagerFactoryBean} v5.0.7.RELEASE
 * Extend {@link EhCacheManagerFactoryBean#afterPropertiesSet()} to support {@link CacheDuration}
 *
 * @author Juergen Hoeller
 * @author Dmitriy Kopylenko
 * @since 1.1.1
 * @see #setConfigLocation
 * @see #setShared
 * @see EhCacheFactoryBean
 * @see net.sf.ehcache.CacheManager
 */
public class EhCacheManagerFactoryBean implements FactoryBean<CacheManager>, InitializingBean, DisposableBean {

    //CHECKSTYLE:OFF

    protected final Log logger = LogFactory.getLog(getClass());

    @Nullable
    private Resource configLocation;

    @Nullable
    private String cacheManagerName;

    private boolean acceptExisting = false;

    private boolean shared = false;

    @Nullable
    private CacheManager cacheManager;

    private boolean locallyManaged = true;


    /**
     * Set the location of the EhCache config file. A typical value is "/WEB-INF/ehcache.xml".
     * <p>Default is "ehcache.xml" in the root of the class path, or if not found,
     * "ehcache-failsafe.xml" in the EhCache jar (default EhCache initialization).
     * @see net.sf.ehcache.CacheManager#create(java.io.InputStream)
     * @see net.sf.ehcache.CacheManager#CacheManager(java.io.InputStream)
     */
    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * Set the name of the EhCache CacheManager (if a specific name is desired).
     * @see net.sf.ehcache.config.Configuration#setName(String)
     */
    public void setCacheManagerName(String cacheManagerName) {
        this.cacheManagerName = cacheManagerName;
    }

    /**
     * Set whether an existing EhCache CacheManager of the same name will be accepted
     * for this EhCacheManagerFactoryBean setup. Default is "false".
     * <p>Typically used in combination with {@link #setCacheManagerName "cacheManagerName"}
     * but will simply work with the default CacheManager name if none specified.
     * All references to the same CacheManager name (or the same default) in the
     * same ClassLoader space will share the specified CacheManager then.
     * @see #setCacheManagerName
     * #see #setShared
     * @see net.sf.ehcache.CacheManager#getCacheManager(String)
     * @see net.sf.ehcache.CacheManager#CacheManager()
     */
    public void setAcceptExisting(boolean acceptExisting) {
        this.acceptExisting = acceptExisting;
    }

    /**
     * Set whether the EhCache CacheManager should be shared (as a singleton at the
     * ClassLoader level) or independent (typically local within the application).
     * Default is "false", creating an independent local instance.
     * <p><b>NOTE:</b> This feature allows for sharing this EhCacheManagerFactoryBean's
     * CacheManager with any code calling <code>CacheManager.create()</code> in the same
     * ClassLoader space, with no need to agree on a specific CacheManager name.
     * However, it only supports a single EhCacheManagerFactoryBean involved which will
     * control the lifecycle of the underlying CacheManager (in particular, its shutdown).
     * <p>This flag overrides {@link #setAcceptExisting "acceptExisting"} if both are set,
     * since it indicates the 'stronger' mode of sharing.
     * @see #setCacheManagerName
     * @see #setAcceptExisting
     * @see net.sf.ehcache.CacheManager#create()
     * @see net.sf.ehcache.CacheManager#CacheManager()
     */
    public void setShared(boolean shared) {
        this.shared = shared;
    }


    @Override
    public void afterPropertiesSet() throws CacheException {
        if (logger.isInfoEnabled()) {
            logger.info("Initializing EhCache CacheManager" +
                (this.cacheManagerName != null ? " '" + this.cacheManagerName + "'" : ""));
        }

        Configuration configuration = (this.configLocation != null ?
            EhCacheManagerUtils.parseConfiguration(this.configLocation) : ConfigurationFactory.parseConfiguration());
        if (this.cacheManagerName != null) {
            configuration.setName(this.cacheManagerName);
        }
        supplementConfigurationWithCacheDuration(configuration);

        if (this.shared) {
            // Old-school EhCache singleton sharing...
            // No way to find out whether we actually created a new CacheManager
            // or just received an existing singleton reference.
            this.cacheManager = CacheManager.create(configuration);
        }
        else if (this.acceptExisting) {
            // EhCache 2.5+: Reusing an existing CacheManager of the same name.
            // Basically the same code as in CacheManager.getInstance(String),
            // just storing whether we're dealing with an existing instance.
            synchronized (CacheManager.class) {
                this.cacheManager = CacheManager.getCacheManager(this.cacheManagerName);
                if (this.cacheManager == null) {
                    this.cacheManager = new CacheManager(configuration);
                }
                else {
                    this.locallyManaged = false;
                }
            }
        }
        else {
            // Throwing an exception if a CacheManager of the same name exists already...
            this.cacheManager = new CacheManager(configuration);
        }
    }

    @Override
    @Nullable
    public CacheManager getObject() {
        return this.cacheManager;
    }

    @Override
    public Class<? extends CacheManager> getObjectType() {
        return (this.cacheManager != null ? this.cacheManager.getClass() : CacheManager.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    @Override
    public void destroy() {
        if (this.cacheManager != null && this.locallyManaged) {
            if (logger.isInfoEnabled()) {
                logger.info("Shutting down EhCache CacheManager" +
                    (this.cacheManagerName != null ? " '" + this.cacheManagerName + "'" : ""));
            }
            this.cacheManager.shutdown();
        }
    }

    //CHECKSTYLE:ON

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EhCacheManagerFactoryBean.class);

    private void supplementConfigurationWithCacheDuration(Configuration configuration) {
        CacheDuration cd;
        String cacheName;
        CacheConfiguration config;

        Set<Class<?>> cdTypes = new Reflections("com.proper").getTypesAnnotatedWith(CacheDuration.class);
        Set<Method> cdMethods = new Reflections("com.proper", new MethodAnnotationsScanner()).getMethodsAnnotatedWith(CacheDuration.class);

        Map<String, CacheDuration> cds = new HashMap<>(cdTypes.size() + cdMethods.size());
        for (Class clz : cdTypes) {
            cd = (CacheDuration) clz.getAnnotation(CacheDuration.class);
            if (cd == null) {
                LOGGER.debug("Could NOT find CacheDuration on {}", clz.getCanonicalName());
                continue;
            }
            cacheName = StringUtils.hasText(cd.cacheName()) ? cd.cacheName() : clz.getCanonicalName();
            cds.put(cacheName, cd);
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
            cds.put(cacheName, cd);
        }
        long tti;
        long ttl;
        for (Map.Entry<String, CacheDuration> entry : cds.entrySet()) {
            cacheName = entry.getKey();
            config = new CacheConfiguration(cacheName, 10000);
            config.setName(cacheName);
            tti = entry.getValue().maxIdleTime() / 1000;
            ttl = entry.getValue().ttl() / 1000;
            config.setTimeToIdleSeconds(tti);
            config.setTimeToLiveSeconds(ttl);
            // 不能将 copyOnRead 设置为 true，否则会影响缓存的 TTI，详见 ExpireTest#testTTI
            config.setCopyOnWrite(true);
            LOGGER.debug("Load {} with {}s ttl and {}s max idle time.", cacheName, ttl, tti);
            configuration.addCache(config);
        }
    }

}
