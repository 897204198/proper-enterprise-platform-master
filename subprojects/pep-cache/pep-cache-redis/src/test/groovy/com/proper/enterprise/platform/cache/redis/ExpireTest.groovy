package com.proper.enterprise.platform.cache.redis

import com.proper.enterprise.platform.cache.redis.service.ExpireTTIService
import com.proper.enterprise.platform.cache.redis.service.ExpireTTLService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ExpireTest extends AbstractTest {

    @Autowired
    private ExpireTTIService ttiService
    @Autowired
    private ExpireTTLService ttlService

    @Before
    void setUp() {
        ttiService.evict()
        ttlService.evict()
    }

    @Test
    void testTTI() {
        synchronized (ExpireTest) {
            5.times {
                assert 1 == ttiService.access()
                sleep(300)
            }
        }
        sleep(800)
        assert 2 == ttiService.access()
    }

    @Test
    void testTTL() {
        synchronized (ExpireTest) {
            3.times {
                assert 1 == ttlService.access()
                sleep(300)
                println "[redis.ExpireTest.testTTL] in sync thread ${Thread.currentThread().getId()}"
            }
        }
        sleep(300)
        println "[redis.ExpireTest.testTTL] out sync thread ${Thread.currentThread().getId()}"
        assert 2 == ttlService.access()
    }

}
