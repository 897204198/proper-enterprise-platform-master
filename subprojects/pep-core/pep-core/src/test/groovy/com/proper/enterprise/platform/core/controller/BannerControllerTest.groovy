package com.proper.enterprise.platform.core.controller

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.http.HttpStatus

class BannerControllerTest extends AbstractTest{
    @Test
    void test() {
        resOfGet("/banner", HttpStatus.OK)
    }
}
