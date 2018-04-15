package com.proper.enterprise.platform.auth.jwt.service.mock

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.service.impl.AbstractUserServiceImpl
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.core.utils.RequestUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service("mockUserService")
@Primary
class MockUserNodeServiceImpl extends AbstractUserServiceImpl {
    def static final DEFAULT_USER = ConfCenter.get("auth.historical.defaultUserId", "PEP_SYS")

    def static final Logger LOGGER = LoggerFactory.getLogger(MockUserNodeServiceImpl.class)

    @Override
    User getCurrentUser() {
        if (ConfCenter.get('test.mockUser.throwEx') == 'true') {
            throw new Exception('Mock to throw exception in getCurrentUser')
        } else {
            def mockUser
            try {
                mockUser = RequestUtil.getCurrentRequest().getAttribute('mockUser')
            } catch (IllegalStateException e) {
                LOGGER.debug("Could not get current request! {}", e.getMessage());
            }
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
                user.setId(DEFAULT_USER)
            }
            return user
        }
    }

}
