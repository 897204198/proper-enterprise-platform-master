package com.proper.enterprise.platform.auth.common.service.mock
import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.service.impl.AbstractUserServiceImpl
import org.springframework.stereotype.Service

@Service("mockUserService")
class MockUserServiceImpl extends AbstractUserServiceImpl {

    @Override
    User getCurrentUser() {
        def mockUser = new UserEntity('MockUserName', 'MockUserPassword')
        mockUser.setId('MockUserId')
        mockUser
    }

}
