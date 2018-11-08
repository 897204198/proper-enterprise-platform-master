package com.proper.enterprise.platform.test

import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TestAbstractIntegrationTest extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(this)

    @Test
    void test() {
        LOGGER.debug('Listening {} ...', port)
        assert port > 0
    }

}
