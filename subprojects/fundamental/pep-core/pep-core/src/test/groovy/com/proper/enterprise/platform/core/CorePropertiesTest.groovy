package com.proper.enterprise.platform.core

import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class CorePropertiesTest extends AbstractSpringTest {

    @Autowired
    private CoreProperties coreProperties

    @Test
    void testDefdatetime() {
        assert "yyyyMMddHHmmss" == coreProperties.getDefaultDatetimeFormat()
        assert coreProperties.getDefaultOperatorId() > ''
    }
}
