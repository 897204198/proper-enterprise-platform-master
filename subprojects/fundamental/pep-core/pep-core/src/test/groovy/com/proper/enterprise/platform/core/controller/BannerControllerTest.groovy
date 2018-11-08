package com.proper.enterprise.platform.core.controller

import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test
import org.springframework.http.HttpStatus

class BannerControllerTest extends AbstractSpringTest {
    @Test
    void test() {
        resOfGet("/banner", HttpStatus.OK)
    }
}
