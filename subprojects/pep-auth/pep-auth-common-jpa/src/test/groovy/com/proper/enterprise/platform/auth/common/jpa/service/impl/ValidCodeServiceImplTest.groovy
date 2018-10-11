package com.proper.enterprise.platform.auth.common.jpa.service.impl

import com.proper.enterprise.platform.api.auth.service.ValidCodeService
import com.proper.enterprise.platform.auth.common.vo.UserVO
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class ValidCodeServiceImplTest extends AbstractTest{

    private static final String URI = '/auth/users'

    @Autowired
    private ValidCodeService validCodeService

    @Test
    void getPasswordValidCode() {
        def userReq = [:]
        userReq['username'] = 'user_dup'
        userReq['name'] = 'name'
        userReq['password'] = 'password'
        userReq['email'] = 'email'
        userReq['phone'] = '123'
        userReq['enable'] = true
        UserVO userVO = JSONUtil.parse(post(URI, JSONUtil.toJSON(userReq), HttpStatus.CREATED).response.contentAsString, UserVO.class)
        def userReq2 = [:]
        userReq2['username'] = 'user_dup2'
        userReq2['name'] = 'name2'
        userReq2['password'] = 'password'
        userReq2['email'] = 'email'
        userReq2['phone'] = '123'
        userReq2['enable'] = true
        UserVO userVO2 = JSONUtil.parse(post(URI, JSONUtil.toJSON(userReq2), HttpStatus.CREATED).response.contentAsString, UserVO.class)
        String validCode1 = validCodeService.getPasswordValidCode("user_dup")
        sleep(1000)
        String validCode2 = validCodeService.getPasswordValidCode("user_dup")
        assert validCode1 == validCode2
        String validCode3 = validCodeService.getPasswordValidCode("user_dup2")
        assert validCode1 != validCode3
    }
}
