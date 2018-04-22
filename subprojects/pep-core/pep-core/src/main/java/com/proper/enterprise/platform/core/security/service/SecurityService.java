package com.proper.enterprise.platform.core.security.service;

/**
 * 框架安全接口
 */
public interface SecurityService {
    /**
     * 获取当前用户id
     * 此方法的实现中禁止使用jpa查询 满足jpa实体监听需求
     *
     * @return 当前用户Id
     */
    String getCurrentUserId();
}
