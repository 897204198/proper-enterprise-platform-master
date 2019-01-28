package com.proper.enterprise.platform.auth.service;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;

import java.io.IOException;

/**
 * Utility service for JSON Web Token (http://jwt.io/)
 * Only support JWT type and HS256 algorithm now
 * Use user id as key of apiSecret caches
 */
public interface JWTService extends AccessTokenService {

    /**
     * 生成token
     * @param header header
     * @param payload payload
     * @return token
     * @throws IOException 抛出异常
     */
    String generateToken(JWTHeader header, JWTPayload payload) throws IOException;

    /**
     * 获取jwt token header
     * @param token token
     * @return JWTHeader
     * @throws IOException 抛出异常
     */
    JWTHeader getHeader(String token) throws IOException;

    /**
     * clearToken
     * @param header header
     */
    void clearToken(JWTHeader header);

}
