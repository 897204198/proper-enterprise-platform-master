package com.proper.enterprise.platform.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.service.AuthcService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AuthcServiceImplTest extends AbstractJPATest{

    @Autowired
    private AuthcService authcService

    /**
     * 测试验证用户名和密码
     */
    @Test
    void testLogin() {
        def result = authcService.authenticate('test', 'pwd')
        assert !result
    }

}
