package com.proper.enterprise.platform.core.security;

import com.proper.enterprise.platform.core.PEPConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;

public class Authentication {

    private Authentication() {
    }

    private static ThreadLocal<String> currentUserThreadLocal = new NamedThreadLocal<>("PEP_CURRENT_USER");

    /**
     * 设置当前登录人id
     *
     * @param currentUserId 当前登录人id
     */
    public static void setCurrentUserId(String currentUserId) {
        currentUserThreadLocal.set(currentUserId);
    }

    /**
     * 获取当前用户id
     * 此方法的实现中禁止使用jpa查询 满足jpa实体监听需求
     *
     * @return 当前用户Id
     */
    public static String getCurrentUserId() {
        String currentUserId = currentUserThreadLocal.get();
        if (StringUtils.isEmpty(currentUserId)) {
            return PEPConstants.DEFAULT_OPERAOTR_ID;
        }
        return currentUserId;
    }

    private static void remove() {
        currentUserThreadLocal.remove();
    }
}
