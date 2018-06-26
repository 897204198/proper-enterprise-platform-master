package com.proper.enterprise.platform.auth.service;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;

import java.io.IOException;

public interface JWTAuthcService {

    /**
     * 获取令牌
     * @param username 用户名
     * @return 令牌
     * @throws IOException io异常
     */
    String getUserToken(String username) throws IOException;

    /**
     * 清除令牌
     * @param username 用户名
     */
    void clearUserToken(String username);

    /**
     * 构建令牌头
     * @param user 用户
     * @return 令牌头
     */
    JWTHeader composeJWTHeader(User user);

    /**
     * Payload part of JSON Web Token
     * @param user 用户
     * @return JWTPayload
     */
    JWTPayload composeJWTPayload(User user);

}
