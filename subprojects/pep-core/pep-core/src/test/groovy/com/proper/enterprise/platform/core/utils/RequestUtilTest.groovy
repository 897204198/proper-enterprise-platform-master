package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class RequestUtilTest extends AbstractTest {

    @Test
    public void couldGetRequest() {
        def uri = '/foo/bar'
        mockRequest.setRequestURI uri
        assert RequestUtil.getCurrentRequest().getRequestURI() == uri
    }

}
