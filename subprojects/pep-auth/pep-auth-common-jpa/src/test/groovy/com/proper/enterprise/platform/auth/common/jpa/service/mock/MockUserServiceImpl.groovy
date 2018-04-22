package com.proper.enterprise.platform.auth.common.jpa.service.mock

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.service.impl.UserServiceImpl
import com.proper.enterprise.platform.core.security.service.SecurityService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.core.utils.RequestUtil
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
class MockUserServiceImpl extends UserServiceImpl implements SecurityService {
    def static final DEFAULT_USER = ConfCenter.get("auth.historical.defaultUserId", "PEP_SYS")
    def static final Logger LOGGER = LoggerFactory.getLogger(MockUserServiceImpl.class)

    @Override
    User getCurrentUser() {
        if (ConfCenter.get('test.mockUser.throwEx') == 'true') {
            throw new Exception('Mock to throw exception in getCurrentUser')
        } else {
            def mockUser = getMockUser()
            def user
            if (mockUser != null) {
                user = this.getByUsername(mockUser.username)
                if (user == null) {
                    user = new UserEntity(mockUser.username, mockUser.password)
                    user.id = mockUser.id
                    user.superuser = mockUser.isSuper
                }
            } else {
                user = new UserEntity('default-mock-user', 'default-mock-user-pwd')
                user.setId('default-mock-user-id')
            }
            return user

        }
    }

    @Override
    String getCurrentUserId() {
        return getMockUser().id
    }

    private getMockUser() {
        try {
            def mockUser = RequestUtil.getCurrentRequest().getAttribute('mockUser')
            if (null == mockUser) {
                mockUser = [:]
                mockUser.id = DEFAULT_USER
                mockUser.username = "default-mock-user"
                mockUser.password = "default-mock-user-pwd"
                mockUser.isSuper = false
            }
            return mockUser
        } catch (IllegalStateException e) {
            LOGGER.debug("Could not get current request! {}", e.getMessage())
        }
    }


}
