package com.proper.enterprise.platform.core.security;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import com.proper.enterprise.platform.core.utils.StringUtil;
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
        if (StringUtil.isEmpty(currentUserId)) {
            return PEPPropertiesLoader.load(CoreProperties.class).getDefaultOperatorId();
        }
        return currentUserId;
    }

    private static void remove() {
        currentUserThreadLocal.remove();
    }
}
