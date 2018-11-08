package com.proper.enterprise.platform.schedule.cluster

import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class TriggerTest extends AbstractJPATest {

    @Autowired
    BusinessService businessService

    @Test
    void test() {
        sleep(1*1000)
        // (1-0.1)/0.2 + 1
        assert businessService.getCount() > 1+2+3
    }

}
