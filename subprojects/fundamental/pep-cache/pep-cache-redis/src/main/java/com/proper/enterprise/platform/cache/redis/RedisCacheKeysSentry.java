package com.proper.enterprise.platform.cache.redis;

import com.proper.enterprise.platform.api.cache.CacheKeysSentry;
import org.redisson.api.RMap;
import org.redisson.client.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Service
public class RedisCacheKeysSentry implements CacheKeysSentry {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheKeysSentry.class);

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Object> keySet(Cache cache) {
        RMap rmap = (RMap) cache.getNativeCache();
        Set result = Collections.EMPTY_SET;
        try {
            result = rmap.readAllKeySet();
        } catch (RedisException ex) {
            LOGGER.debug("Could not serialize key from {}", cache.getName());
        }
        return result;
    }

}
