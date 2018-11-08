package com.proper.enterprise.platform.cache.ehcache

import com.proper.enterprise.platform.cache.ehcache.service.CacheableService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class SpringCacheTest extends AbstractJPATest {

    @Autowired
    CacheableService service

    @Test
    void testCacheDuration() {
        def t1 = service.testWithCacheName()
        assert t1 == service.testWithCacheName()
        sleep(1000)
        assert t1 != service.testWithCacheName()

        def t2 = service.testWithoutCacheName()
        assert t2 == service.testWithoutCacheName()
        sleep(1010)
        assert t2 != service.testWithoutCacheName()
        service.evit()
    }

    @Test
    void testListCache() {
        def list = service.getList()
        list.add('c')
        assert !service.getList().contains('c')
    }

}
