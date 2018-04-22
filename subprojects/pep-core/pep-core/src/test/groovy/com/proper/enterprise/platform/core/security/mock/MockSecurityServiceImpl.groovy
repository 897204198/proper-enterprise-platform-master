package com.proper.enterprise.platform.core.security.mock

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.security.service.SecurityService
import org.springframework.stereotype.Service

@Service(value = "securityService")
class MockSecurityServiceImpl implements SecurityService {

    @Override
    String getCurrentUserId() {
        return "mock"+PEPConstants.DEFAULT_OPERAOTR_ID
    }
}
