package com.proper.enterprise.platform.auth.common.service.mock

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.service.impl.AbstractUserServiceImpl
import com.proper.enterprise.platform.core.utils.ConfCenter
import org.springframework.stereotype.Service

@Service("mockUserService")
class MockUserServiceImpl extends AbstractUserServiceImpl {

    @Override
    User getCurrentUser() {
        String throwEx = ConfCenter.get('test.throwEx', 'false')
        if (throwEx == 'true') {
            throw new Exception('Mock to throw exception')
        } else {
            def mockUser = new UserEntity('MockUserName', 'MockUserPassword')
            mockUser.setId('MockUserId')
            return mockUser
        }
    }

}
