package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.AccessToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 访问资源的 token 服务
 * token 可以是自定义的 token
 * 也可以是按某种规范实现的 token（如 JWT）
 */
public interface AccessTokenService {

    /**
     * 请求头中的 token 标识
     */
    String TOKEN_FLAG_HEADER = "X-PEP-TOKEN";
    /**
     * 请求头中的 token 标识
     * 兼容 v0.2.2 之前的标识
     */
    String TOKEN_FLAG_HEADER_COMPATIBILITY = "Authorization";
    /**
     * Bearer Token 关键字
     */
    String TOKEN_FLAG_HEADER_BEARER = "Bearer";
    /**
     * Cookie 中的 token 标识
     */
    String TOKEN_FLAG_COOKIE = "X-PEP-TOKEN";
    /**
     * URI 中的 token 标识
     */
    String TOKEN_FLAG_URI = "access_token";

    /**
     * 从请求中获取 token，按如下顺序及位置获取
     * 1. 请求头
     * 2. Cookie
     * 3. 请求参数
     * 若从以上位置中均未获得到 token，则返回空
     *
     * @param request 请求
     * @return token 字符串或空
     */
    Optional<String> getToken(HttpServletRequest request);

    /**
     * 校验 token 是否合法
     * 仅用作身份校验，并不校验权限
     *
     * @param token token
     * @return true：合法；false：非法
     */
    default boolean verify(String token) {
        return false;
    }

    /**
     * 获取 token 代表的用户 ID
     * 无法获取时，返回空
     *
     * @param token token 字符串
     * @return 用户 ID 或空
     */
    Optional<String> getUserId(String token);

    /**
     * 根据用户 ID 获取 token 模型
     * 无法获取时，返回空
     *
     * @param userId 用户 ID
     * @return token 模型或空
     */
    Optional<AccessToken> getByUserId(String userId);

    /**
     * 生成一个 token 字符串
     *
     * @return 新生成的 token 字符串
     */
    String generate();

    /**
     * 保存或更新 token 模型
     *
     * @param accessToken token 模型
     * @return 保存或更新后的 token 模型
     */
    AccessToken saveOrUpdate(AccessToken accessToken);

    /**
     * 根据 token 字符串删除 access token 模型
     *
     * @param token token 字符串
     */
    void deleteByToken(String token);

}
