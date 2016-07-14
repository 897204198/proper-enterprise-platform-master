package com.proper.enterprise.platform.auth.common.service.mock

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.service.impl.CommonUserServiceImpl
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.core.utils.RequestUtil
import org.springframework.stereotype.Service

@Service("mockUserService")
class MockUserServiceImpl extends CommonUserServiceImpl {

    @Override
    User getCurrentUser() {
        if (ConfCenter.get('test.mockUser.throwEx') == 'true') {
            throw new Exception('Mock to throw exception in getCurrentUser')
        } else {
            def mockUser = RequestUtil.getCurrentRequest().getAttribute('mockUser')
            def user = null
            if (mockUser != null) {
                user = new UserEntity(mockUser.username, mockUser.password)
                user.id = mockUser.id
                user.superuser = mockUser.isSuper
            } else {
                user = new UserEntity('default-mock-user', 'default-mock-user-pwd')
                user.setId('default-mock-user-id')
            }
            return user
        }
    }

}
