package com.proper.enterprise.platform.cache.ehcache;

import net.sf.ehcache.constructs.web.ShutdownListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ResourceLoader;

import javax.servlet.ServletContextListener;

@EnableCaching
@Configuration
@PropertySource(value = "classpath:application-cache-ehcache.properties")
public class EhCacheConfiguration {

    @Value("${spring.cache.ehcache.config}")
    private String ehcacheConfigLoc;

    @Bean
    public EhCacheManagerFactoryBean ehcacheCacheManager(ResourceLoader resourceLoader) {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setCacheManagerName("pepSpringCacheManager");
        ehCacheManagerFactoryBean.setAcceptExisting(true);
        ehCacheManagerFactoryBean.setConfigLocation(resourceLoader.getResource(ehcacheConfigLoc));
        return ehCacheManagerFactoryBean;
    }

    @Bean
    public EhCacheCacheManager cacheManager(EhCacheManagerFactoryBean ehcacheCacheManager) {
        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
        ehCacheCacheManager.setCacheManager(ehcacheCacheManager.getObject());
        return ehCacheCacheManager;
    }

    /**
     * 通知ehcache即将停止服务
     * 停止服务时持久化缓存数据
     *
     * @return 监听器
     */
    @Bean
    public ServletContextListener ehcacheShutdownListener() {
        return new ShutdownListener();
    }

}
