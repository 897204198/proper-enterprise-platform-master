package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.service.ValidCodeService;
import com.proper.enterprise.platform.api.cache.CacheDuration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ValidCodeServiceImpl implements ValidCodeService {

    private static int digits = 6;

    @Override
    @Cacheable(cacheNames = "retrievePasswordValidCode", key = "#username")
    @CacheDuration(cacheName = "retrievePasswordValidCode", ttl = 900000L)
    public String getPasswordValidCode(String username) {
        StringBuilder validCode = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            validCode.append(Integer.toString(new Random().nextInt(9)));
        }
        return validCode.toString();
    }
}
