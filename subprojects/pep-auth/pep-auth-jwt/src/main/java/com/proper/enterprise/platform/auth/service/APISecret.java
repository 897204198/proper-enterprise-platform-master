package com.proper.enterprise.platform.auth.service;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class APISecret {

    private static final Logger LOGGER = LoggerFactory.getLogger(APISecret.class);

    private static final String CACHE_NAME = "apiSecrets";

    @Cacheable(value = CACHE_NAME)
    public String getAPISecret(String key) {
        LOGGER.debug("Generate new API secret for key: {}", key);
        String apiSecret = key;
        for (int i = 0; i < 3; i++) {
            apiSecret = md5Base64(apiSecret + System.nanoTime());
        }
        return apiSecret;
    }

    private String md5Base64(String message) {
        return Base64.encodeBase64URLSafeString(DigestUtils.md5(message));
    }

    @CacheEvict(value = CACHE_NAME)
    public void clearAPISecret(String key) {
        // spring evict the cache, no need to do anything more
        LOGGER.debug("Clear API secret cache with key: {}", key);
    }

}
