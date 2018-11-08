package com.proper.enterprise.platform.api.auth.service;

import javax.servlet.http.HttpServletRequest;

public interface AuthzService {

    /**
     * 根据请求信息及拦权限则判断是否忽略权限
     *
     * @param  request 请求
     * @return 是否忽略拦截
     */
    boolean shouldIgnore(HttpServletRequest request);

    /**
     * 用户是否有访问请求中资源的权限
     *
     * @param  request 请求
     * @param  userId  用户 ID
     * @return 是否有访问权限
     */
    boolean accessible(HttpServletRequest request, String userId);

    /**
     * 用户是否有访问资源描述所代表的资源的权限
     *
     * @param  resDescriptor 资源描述，格式为 method:url，如：GET:/foo/bar
     * @param  userId        用户 ID
     * @return 是否有访问权限
     */
    boolean accessible(String resDescriptor, String userId);

}
