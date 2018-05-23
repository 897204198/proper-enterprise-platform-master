package com.proper.enterprise.platform.api.auth.service;

public interface AuthzService {
    /**
     * 是否忽略拦截
     *
     * @param url        请求url
     * @param method     请求方法
     * @param hasContext 包含的内容
     * @return 是否忽略拦截
     */
    boolean shouldIgnore(String url, String method, boolean hasContext);

    /**
     * 是否有操作当前资源的权限
     *
     * @param url        请求url
     * @param method     请求方法
     * @param hasContext 包含的内容
     * @param userId     用户id
     * @return 是否有操作当前资源的权限
     */
    boolean accessible(String url, String method, boolean hasContext, String userId);

}
