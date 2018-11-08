package org.hibernate.cache.ehcache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.internal.util.HibernateEhcacheUtils;

import java.net.URL;
import java.util.Properties;

/**
 * 重写EhCacheRegionFactory start方法当集成测试与单元测试一起执行的时候会出现ehcache manager加载重复的问题
 * 解决重复加载 若发现cacheManagerMaps中包含了同名manager则直接返回
 */
public class PEPTestEhCacheRegionFactory extends EhCacheRegionFactory {

    @Override
    public void start(SessionFactoryOptions settings, Properties properties) throws CacheException {
        this.settings = settings;
        if (manager != null) {
            return;
        }

        try {
            String configurationResourceName = null;
            if (properties != null) {
                configurationResourceName = (String) properties.get(NET_SF_EHCACHE_CONFIGURATION_RESOURCE_NAME);
            }
            if (configurationResourceName == null || configurationResourceName.length() == 0) {
                final Configuration configuration = ConfigurationFactory.parseConfiguration();
                manager = new CacheManager(configuration);
            } else {
                final URL url = loadResource(configurationResourceName);
                final Configuration configuration = HibernateEhcacheUtils.loadAndCorrectConfiguration(url);
                //注入前在缓存区查找
                for (CacheManager cacheManager : CacheManager.ALL_CACHE_MANAGERS) {
                    if (cacheManager.getName().equals(configuration.getName())) {
                        manager = cacheManager;
                        return;
                    }
                }
                manager = new CacheManager(configuration);
            }
            mbeanRegistrationHelper.registerMBean(manager, properties);
        } catch (net.sf.ehcache.CacheException e) {
            if (e.getMessage().startsWith(
                "Cannot parseConfiguration CacheManager. Attempt to create a new instance of "
                    + "CacheManager using the diskStorePath"
            )) {
                throw new CacheException(
                    "Attempt to restart an already started EhCacheRegionFactory. "
                        + "Use sessionFactory.close() between repeated calls to buildSessionFactory. "
                        + "Consider using SingletonEhCacheRegionFactory. Error from ehcache was: " + e.getMessage()
                );
            } else {
                throw new CacheException(e);
            }
        }
    }

}
