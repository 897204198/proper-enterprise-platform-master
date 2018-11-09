package com.proper.enterprise.platform.notice.server.api.util;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.notice.server.api.service.AppDaoService;

public class AppUtil {

    private AppUtil() {
    }

    /**
     * app是否为启用状态 且app未被删除
     *
     * @param appKey 应用唯一标识
     * @return true false
     */
    public static boolean isEnable(String appKey) {
        return PEPApplicationContext.getBean(AppDaoService.class).isEnable(appKey);
    }
}
