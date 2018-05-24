package com.proper.enterprise.platform.oopsearch.mock

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.security.service.SecurityService
import com.proper.enterprise.platform.core.utils.RequestUtil
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
class MockSecurityServiceImpl implements SecurityService {

    @Override
    String getCurrentUserId() {
        return getMockUser().id;
    }


    private getMockUser() {
        try {
            def mockUser = RequestUtil.getCurrentRequest().getAttribute('mockUser')
            if (null == mockUser) {
                mockUser = [:]
                mockUser.id = PEPConstants.DEFAULT_OPERAOTR_ID
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

