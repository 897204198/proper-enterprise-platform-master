package com.proper.enterprise.platform.auth.jwt;

import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;
import com.proper.enterprise.platform.core.json.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility service for JSON Web Token (http://jwt.io/)
 * Only support JWT type and HS256 algorithm now
 * Use user id as key of apiSecret caches
 * 
 * @author Hinex
 */
@Service
public class JWTService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTService.class);

    public static final String CACHE_SECRETS = "apiSecrets";

    @Autowired
    private CacheManager cacheManager;

    public String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtil.isNotNull(token) && token.contains("Bearer")) {
            token = token.replace("Bearer", "").trim();
        }
        return token;
    }

    public String generateToken(JWTHeader header, JWTPayload payload) {
        String apiSecret = generateAPISecret(header.getUid());
        String headerStr = JSONUtil.toJSONString(header);
        String payloadStr = JSONUtil.toJSONString(payload);
        String sign = hmacSha256Base64(apiSecret, StringUtil.join('.', headerStr, payloadStr));
        return StringUtil.join('.', headerStr, payloadStr, sign);
    }

    private String hmacSha256Base64(String secret, String message) {
        return Base64.encodeBase64URLSafeString(HmacUtils.hmacSha256(secret, message));
    }

    @CachePut(value = CACHE_SECRETS, key = "#key")
    public String generateAPISecret(String key) {
        String apiSecret = key;
        for (int i = 0; i < 3; i++) {
            apiSecret = md5Base64(apiSecret + System.currentTimeMillis());
        }
        return apiSecret;
    }

    private String md5Base64(String message) {
        return Base64.encodeBase64URLSafeString(DigestUtils.md5(message));
    }

    @Cacheable(value = CACHE_SECRETS, key = "#key")
    public String getAPISecret(String key) {
        Cache cache = cacheManager.getCache(CACHE_SECRETS);
        if (cache.get(key) != null) {
            return (String) cache.get(key).get();
        } else {
            return generateAPISecret(key);
        }
    }

    @CacheEvict(value = CACHE_SECRETS, key = "#key")
    public void clearAPISecret(String key) {
        // spring evict the cache, no need to do nothing more
    }
    
    public boolean verify(String token) {
        boolean result = false;
        if (StringUtil.isNotNull(token) || !token.contains(".")) {
            return result;
        }
        
        String[] split = token.split("\\.");
        String headerStr = split[0];
        String payloadStr = split[1];
        String sign = split[2];
        
        try {
            JWTHeader header = getHeader(token);
            String apiSecret = getAPISecret(header.getUid());
            result = sign.equals(hmacSha256Base64(apiSecret, StringUtil.join('.', headerStr, payloadStr)));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
    
    public JWTHeader getHeader(String token) throws Exception {
        String[] split = token.split("\\.");
        String headerStr = split[0];
        return JSONUtil.parseObject(Base64.decodeBase64(headerStr), JWTHeader.class);
    }
    
}
