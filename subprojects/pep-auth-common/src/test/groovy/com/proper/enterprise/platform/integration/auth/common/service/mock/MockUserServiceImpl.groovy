package com.proper.enterprise.platform.integration.auth.common.service.mock

import com.proper.enterprise.platform.api.auth.User
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.service.impl.AbstractUserServiceImpl
import org.springframework.stereotype.Service

@Service
class MockUserServiceImpl extends AbstractUserServiceImpl {

    @Override
    User getCurrentUser() {
        def mockUser = new UserEntity('MockUserName', 'MockUserPassword')
        mockUser.setId('MockUserId')
        mockUser
    }

}
