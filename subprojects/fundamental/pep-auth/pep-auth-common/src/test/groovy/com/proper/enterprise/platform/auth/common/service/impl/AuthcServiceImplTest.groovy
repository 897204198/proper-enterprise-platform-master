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
    void testlogin() {

        def result = authcService.authenticate('test', 'pwd');
        assert false == result
    }

    /**
     * 获取账号密码
     */
    @Test
    void getUserNameAndPwd() {
        def usermap = new HashMap();
        usermap.put('username', 'test')
        usermap.put('pwd', 'test')
        def username = authcService.getUsername(usermap)
        assert 'test' == username
        def password = authcService.getPassword(usermap)
        assert 'test' == password
    }

}
