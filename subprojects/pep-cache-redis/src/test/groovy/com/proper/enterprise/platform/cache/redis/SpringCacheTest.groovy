package com.proper.enterprise.platform.cache.redis
import com.proper.enterprise.platform.cache.redis.service.CachableService
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.test.AbstractTest
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.data.redis.core.RedisOperations

import java.util.concurrent.TimeUnit

class SpringCacheTest extends AbstractTest  {

    @Autowired
    CachableService service

    @Autowired
    CacheManager cacheManager

    @Before
    public void setUp() {
        service.clear()
    }

    @Test
    public void checkRedisCacheWorks() {
        4.times {
            assert service.addCount() == 6
        }

        service.clear()
        4.times {
            assert service.addCount() == 7
        }

        assert service.newKey() == 10
    }

    @Test
    public void checkDefaultExpiration() {
        service.addCount()

        RedisOperations ro = cacheManager.getCache('test').getNativeCache()
        def expire = ro.getExpire('count', TimeUnit.MILLISECONDS)
        assert expire > 0 && expire < ConfCenter.getInt('cache.redis.defaultExpiration') * 1000
    }

    @Test
    public void checkKeyExpiration() {
        Cache cache = cacheManager.getCache('apiSecrets')
        RedisOperations ro = cache.getNativeCache()
        def keys = [], expires = [], i = 0
        5.times { idx ->
            keys[idx] = RandomStringUtils.randomAlphabetic(10)
            service.setKey(keys[idx])
            sleep(20)
        }

        keys.each { key ->
            expires[i++] = ro.getExpire(key, TimeUnit.MICROSECONDS)
        }

        4.times { idx ->
            assert expires[idx] < expires[idx + 1]
        }
        cache.clear()
    }

}
