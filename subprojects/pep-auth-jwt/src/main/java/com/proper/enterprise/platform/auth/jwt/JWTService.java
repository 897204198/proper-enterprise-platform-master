package com.proper.enterprise.platform.auth.jwt;

import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.core.conf.Constants;
import com.proper.enterprise.platform.core.json.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * Utility service for JSON Web Token (http://jwt.io/)
 * Only support JWT type and HS256 algorithm now
 * 
 * @author Hinex
 */
@Service
public class JWTService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTService.class);

    public static final String CACHE_NAME = "apiSecrets";

    @Autowired
    private CacheManager cacheManager;

    @CachePut(value = CACHE_NAME, key = "#key")
    public String generateAPISecret(String key) {
        String apiSecret = key;
        for (int i = 0; i < 3; i++) {
            apiSecret = base64MD5(apiSecret + System.currentTimeMillis());
        }
        return apiSecret;
    }

    private String base64MD5(String message) {
        String result = "";
        try {
            result = Base64.encodeBase64URLSafeString(DigestUtils.md5(message.getBytes(Constants.ENCODING)));
        } catch (UnsupportedEncodingException uee) {
            LOGGER.error("Encode message error!", uee);
        }
        return result;
    }

    @Cacheable(value = CACHE_NAME, key = "#key")
    public String getAPISecret(String key) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache.get(key) != null) {
            return (String) cache.get(key).get();
        } else {
            return generateAPISecret(key);
        }
    }

    @CacheEvict(value = CACHE_NAME, key = "#key")
    public void clearAPISecret(String key) {
        // spring evict the cache, no need to do nothing more
    }
    
    public boolean verify(String jwt) {
        boolean result = false;
        if (StringUtil.isNotNull(jwt) || !jwt.contains(".")) {
            return result;
        }
        
        String[] split = jwt.split("\\.");
        String headerStr = split[0];
        String payloadStr = split[1];
        String sign = split[2];
        
        try {
            JWTHeader header = getHeader(jwt);
            // Use user id as API key
            String apiSecret = getAPISecret(header.getUid());
            result = hs256(headerStr + "." + payloadStr, apiSecret).equals(sign);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
    
    public JWTHeader getHeader(String jwt) throws Exception {
        String[] split = jwt.split("\\.");
        String headerStr = split[0];
        return mapper.readValue(Base64.decodeBase64(headerStr), JWTHeader.class);
    }
    
    public String hs256(String message, String secret) {
        String result = "";
        try {
            SecretKey key = new SecretKeySpec(secret.getBytes(Constants.ENCODING), "HmacSHA256");
            Mac mac = Mac.getInstance(key.getAlgorithm());
            mac.init(key);
            byte[] sign = mac.doFinal(message.getBytes(Constants.ENCODING));
            result = Base64.encodeBase64URLSafeString(sign);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
    
    public String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.contains("Bearer")) {
            token = token.replace("Bearer", "").trim();
        }
        return token;
    }
    
}
