package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;

/**
 * 访问 token 模型
 */
public interface AccessToken extends IBase {

    /**
     * 每个 token 代表一个用户
     * 每个用户有一个唯一的用户 ID
     *
     * @return 用户 ID
     */
    String getUserId();

    /**
     * 赋值 userId
     *
     * @param userId 用户 ID
     */
    void setUserId(String userId);

    /**
     * 用于描述该 token 的作用
     *
     * @return token 名称
     */
    String getName();

    /**
     * 赋值 name
     *
     * @param name token 名称
     */
    void setName(String name);

    /**
     * token 字符串，需保证唯一性
     *
     * @return token
     */
    String getToken();

    /**
     * 赋值 token
     *
     * @param token token
     */
    void setToken(String token);

    /**
     * 通过 METHOD:URI 的方式描述 token 可以访问的资源
     * 多资源通过英文逗号（,）间隔
     * 如：POST:/auth/login,GET:/auth/menus
     *
     * @return 对该 token 可访问的资源描述
     */
    String getResourcesDescription();

    /**
     * 赋值访问的资源描述 ResourcesDescription
     *
     * @param resourcesDescription 可访问的资源描述
     */
    void setResourcesDescription(String resourcesDescription);

}
