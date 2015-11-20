package com.proper.enterprise.platform.auth.jwt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public class JWT {

    public static final String CACHE_SECRETS = "apiSecrets";

    @Cacheable(value = CACHE_SECRETS)
    public String getAPISecret(String key) {
        String apiSecret = key;
        for (int i = 0; i < 3; i++) {
            apiSecret = md5Base64(apiSecret + System.currentTimeMillis());
        }
        return apiSecret;
    }

    private String md5Base64(String message) {
        return Base64.encodeBase64URLSafeString(DigestUtils.md5(message));
    }

    @CacheEvict(value = CACHE_SECRETS)
    public void clearAPISecret(String key) {
        // spring evict the cache, no need to do nothing more
    }

}
