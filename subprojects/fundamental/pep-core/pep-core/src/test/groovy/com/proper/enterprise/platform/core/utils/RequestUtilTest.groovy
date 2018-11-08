package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test

class RequestUtilTest extends AbstractSpringTest {

    @Test
    public void couldGetRequest() {
        def uri = '/foo/bar'
        mockRequest.setRequestURI uri
        assert RequestUtil.getCurrentRequest().getRequestURI() == uri
    }

}
