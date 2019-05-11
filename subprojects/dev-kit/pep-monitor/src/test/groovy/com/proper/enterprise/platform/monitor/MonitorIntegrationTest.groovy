package com.proper.enterprise.platform.monitor

import com.proper.enterprise.platform.test.AbstractIntegrationTest
import org.junit.Test
import org.springframework.http.HttpStatus

class MonitorIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() {
        get('/druid', HttpStatus.OK)
        get('/actuator', HttpStatus.OK)
        get('/monitoring', HttpStatus.OK)
    }

}
