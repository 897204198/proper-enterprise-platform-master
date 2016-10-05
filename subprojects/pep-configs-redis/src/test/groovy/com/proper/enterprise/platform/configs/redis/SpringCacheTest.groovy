package com.proper.enterprise.platform.configs.redis
import com.proper.enterprise.platform.configs.redis.service.CachableService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class SpringCacheTest extends AbstractTest  {

    @Autowired
    CachableService service

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

}
