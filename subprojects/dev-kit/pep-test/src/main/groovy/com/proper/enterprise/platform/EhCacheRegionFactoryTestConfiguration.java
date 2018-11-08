package com.proper.enterprise.platform;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 当存在ehcache的cacheManager
 * 将EhCacheRegionFactory修改为PEPTestEhCacheRegionFactory
 * 解决cacheManager重复加载
 */
@Configuration
@ConditionalOnClass(CacheManager.class)
public class EhCacheRegionFactoryTestConfiguration {

    private JpaProperties jpaProperties;

    @Autowired
    public EhCacheRegionFactoryTestConfiguration(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean
    @Primary
    public DruidDataSource dataSourceTest() {
        jpaProperties.getProperties().put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.PEPTestEhCacheRegionFactory");
        return DruidDataSourceBuilder.create().build();
    }
}
