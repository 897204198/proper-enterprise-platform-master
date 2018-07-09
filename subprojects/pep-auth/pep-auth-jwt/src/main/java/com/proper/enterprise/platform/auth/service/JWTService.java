package com.proper.enterprise.platform.auth.service;

import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Utility service for JSON Web Token (http://jwt.io/)
 * Only support JWT type and HS256 algorithm now
 * Use user id as key of apiSecret caches
 *
 * @author Hinex
 */
public interface JWTService {

    /**
     * 获取token
     * @param request 请求参数
     * @return token
     */
    String getToken(HttpServletRequest request);

    /**
     * 在Header中获取token
     * @param request 请求参数
     * @return token
     */
    String getTokenFromHeader(HttpServletRequest request);

    /**
     * 在Cookie中获取token
     * @param request 请求参数
     * @return token
     */
    String getTokenFromCookie(HttpServletRequest request);

    /**
     * 在请求参数的中获取token
     * @param request 请求参数
     * @return token
     */
    String getTokenFromReqParameter(HttpServletRequest request);

    /**
     * 生成token
     * @param header header
     * @param payload payload
     * @return token
     * @throws IOException 抛出异常
     */
    String generateToken(JWTHeader header, JWTPayload payload) throws IOException;

    /**
     * 校验token是否正确
     * @param token token
     * @return boolean
     * @throws IOException 抛出异常
     */
    boolean verify(String token) throws IOException;

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
