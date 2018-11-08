package com.proper.enterprise.platform.cache.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
@EnableCaching
@PropertySource(value = "classpath:application-cache-redis.properties")
public class RedisConfiguration {

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson(@Value("classpath:conf/cache/redis/redisson.yaml") Resource configFile) throws IOException {
        Config config = Config.fromYAML(configFile.getInputStream());
        return Redisson.create(config);
    }

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        return new RedisCacheManager(redissonClient, "classpath:conf/cache/redis/redisson-config.yaml");
    }

}
