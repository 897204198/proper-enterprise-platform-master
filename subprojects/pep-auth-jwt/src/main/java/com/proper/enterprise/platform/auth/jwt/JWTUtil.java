package com.proper.enterprise.platform.auth.jwt;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for JSON Web Token (http://jwt.io/)
 * 
 * @author Hinex
 */
public class JWTUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtil.class);
    
    private static final String ENCODING = "utf8";
    private static Map<String, String> cache = new HashMap<String, String>();
    private static ObjectMapper mapper = new ObjectMapper();
    
    private JWTUtil() { }
    
    public static String getAPIKey(String appId) {
        return hexMD5("alpha" + appId + "hinex");
    }
    
    private static String hexMD5(String message) {
        String result = "";
        try {
            result = Base64.encodeBase64URLSafeString((MD5.md5(message.getBytes(ENCODING))));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
    
    public static String generateAPISecret(String apiKey) {
        String apiSecret = apiKey;
        for (int i = 0; i < 3; i++) {
            apiSecret = hexMD5(apiSecret + System.currentTimeMillis());
        }
        cache.put(apiKey, apiSecret);
        return apiSecret;
    }
    
    public static String getAPISecret(String apiKey) {
        if (cache.containsKey(apiKey)) {
            return cache.get(apiKey);
        } else {
            return generateAPISecret(apiKey);
        }
    }
    
    public static boolean verify(String jwt) {
        boolean result = false;
        if (jwt == null || jwt.isEmpty() || !jwt.contains(".")) {
            return result;
        }
        
        String[] split = jwt.split("\\.");
        String headerStr = split[0];
        String payloadStr = split[1];
        String sign = split[2];
        
        try {
            JWTHeader header = getHeader(jwt);
            if (!header.getTyp().equalsIgnoreCase(JWTHeader.DEFAULT_TYP) || 
                    !header.getAlg().equalsIgnoreCase(JWTHeader.DEFAULT_ALG)) {
                LOGGER.error("Not support type ({}) or algorithm ({}).", header.getTyp(), header.getAlg());
                return result;
            }
            
            JWTPayload payload = getPayload(jwt);
            String apiKey = payload.getApikey();
            String apiSecret = getAPISecret(apiKey);
            result = hs256(headerStr + "." + payloadStr, apiSecret).equals(sign);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
    
    public static JWTHeader getHeader(String jwt) throws Exception {
        String[] split = jwt.split("\\.");
        String headerStr = split[0];
        return mapper.readValue(Base64.decodeBase64(headerStr), JWTHeader.class);
    }
    
    public static JWTPayload getPayload(String jwt) throws Exception {
        String[] split = jwt.split("\\.");
        String payloadStr = split[1];
        return mapper.readValue(Base64.decodeBase64(payloadStr), JWTPayload.class);
    }
    
    public static String hs256(String message, String secret) {
        String result = "";
        try {
            SecretKey key = new SecretKeySpec(secret.getBytes(ENCODING), "HmacSHA256");
            Mac mac = Mac.getInstance(key.getAlgorithm());
            mac.init(key);
            byte[] sign = mac.doFinal(message.getBytes(ENCODING));
            result = Base64.encodeBase64URLSafeString(sign);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
    
    public static String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.contains("Bearer")) {
            token = token.replace("Bearer", "").trim();
        }
        return token;
    }
    
}
