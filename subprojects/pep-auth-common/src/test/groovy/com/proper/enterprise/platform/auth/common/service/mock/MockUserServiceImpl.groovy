package com.proper.enterprise.platform.auth.common.service.mock

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.service.impl.CommonUserServiceImpl
import com.proper.enterprise.platform.core.utils.ConfCenter
import org.springframework.stereotype.Service

@Service("mockUserService")
class MockUserServiceImpl extends CommonUserServiceImpl {

    @Override
    User getCurrentUser() {
        if (ConfCenter.get('test.throwEx') == 'true') {
            throw new Exception('Mock to throw exception in getCurrentUser')
        } else {
            def mockUser = new UserEntity('MockUserName', 'MockUserPassword')
            mockUser.setId('MockUserId')
            mockUser.setSuperuser(true)
            return mockUser
        }
    }

}
