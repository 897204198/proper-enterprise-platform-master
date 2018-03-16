package com.proper.enterprise.platform.auth.common.neo4j.service.mock

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.auth.common.service.impl.AbstractUserServiceImpl
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity


import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.core.utils.RequestUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service("mockUserService")
@Primary
class MockUserNodeServiceImpl extends AbstractUserServiceImpl {

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
                    user = new UserNodeEntity(mockUser.username, mockUser.password)
                    user.id = mockUser.id
                    user.superuser = mockUser.isSuper
                }
            } else {
                user = new UserNodeEntity('default-mock-user', 'default-mock-user-pwd')
                user.setId('default-mock-user-id')
            }
            return user
        }
    }

}
