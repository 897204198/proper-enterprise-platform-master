package com.proper.enterprise.platform.core.security.util;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.security.service.SecurityService;
import org.apache.commons.lang3.StringUtils;

public class SecurityUtil {

    public static String getCurrentUserId() {
        try {
            String currentUserId = PEPApplicationContext.getApplicationContext().getBean(SecurityService.class).getCurrentUserId();
            if (StringUtils.isEmpty(currentUserId)) {
                return PEPConstants.DEFAULT_OPERAOTR_ID;
            }
            return currentUserId;
        } catch (Exception e) {
            return PEPConstants.DEFAULT_OPERAOTR_ID;
        }
    }
}
