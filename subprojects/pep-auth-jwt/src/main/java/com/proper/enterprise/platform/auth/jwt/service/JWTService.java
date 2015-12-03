package com.proper.enterprise.platform.auth.jwt.service;

import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;
import com.proper.enterprise.platform.core.conf.Constants;
import com.proper.enterprise.platform.core.json.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility service for JSON Web Token (http://jwt.io/)
 * Only support JWT type and HS256 algorithm now
 * Use user id as key of apiSecret caches
 * 
 * @author Hinex
 */
public class JWTService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTService.class);

    private APISecret secret;

    public void setSecret(APISecret secret) {
        this.secret = secret;
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtil.isNotNull(token) && token.contains("Bearer")) {
            token = token.replace("Bearer", "").trim();
        }
        return token;
    }

    public String generateToken(JWTHeader header, JWTPayload payload) {
        String apiSecret = secret.getAPISecret(header.getId());
        String headerStr = JSONUtil.toJSONString(header);
        String payloadStr = JSONUtil.toJSONString(payload);
        LOGGER.debug("apiSecret: {}, header: {}, payload: {}", apiSecret, headerStr, payloadStr);
        String headerBase64 = base64(headerStr);
        String payloadBase64 = base64(payloadStr);
        String sign = hmacSha256Base64(apiSecret, headerBase64 + "." + payloadBase64);
        return StringUtil.join(new String[]{headerBase64, payloadBase64, sign}, ".");
    }

    private String hmacSha256Base64(String secret, String message) {
        String result = Base64.encodeBase64URLSafeString(HmacUtils.hmacSha256(secret, message));
        LOGGER.debug("API secrect is {}, message is {} and result is {}", secret, message, result);
        return result;
    }

    private String base64(String str) {
        return Base64.encodeBase64URLSafeString(str.getBytes(Constants.DEFAULT_CHARSET));
    }

    public boolean verify(String token) {
        if (StringUtil.isNull(token) || !token.contains(".")) {
            LOGGER.debug("Token should NOT NULL!");
            return false;
        }
        
        String[] split = token.split("\\.");
        String headerBase64= split[0];
        String payloadBase64 = split[1];
        String sign = split[2];
        
        JWTHeader header = getHeader(token);
        if (header == null) {
            LOGGER.debug("Want to parse header from token, but get NULL! Maybe token is INVALID!");
            return false;
        }
        String apiSecret = secret.getAPISecret(header.getId());
        if (!sign.equals(hmacSha256Base64(apiSecret, headerBase64 + "." + payloadBase64))) {
            LOGGER.debug("Token is INVALID or EXPIRED! Sign is {}", sign);
            return false;
        }

        return true;
    }
    
    public JWTHeader getHeader(String token) {
        String[] split = token.split("\\.");
        String headerStr = split[0];
        return JSONUtil.parseObject(Base64.decodeBase64(headerStr), JWTHeader.class);
    }
    
}
