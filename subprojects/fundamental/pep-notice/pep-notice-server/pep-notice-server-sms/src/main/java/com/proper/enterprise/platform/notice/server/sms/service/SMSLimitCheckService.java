package com.proper.enterprise.platform.notice.server.sms.service;

import com.proper.enterprise.platform.api.cache.CacheDuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 短信限制检查服务
 * 目前的限制为，10 分钟内只能发送三条以内的短信
 */
@Service
@CacheConfig(cacheNames = "pep.notice.SMSLimit")
@CacheDuration(cacheName = "pep.notice.SMSLimit", ttl = 10 * 60 * 1000, maxIdleTime = 10 * 60 * 1000)
public class SMSLimitCheckService {

    @Autowired
    private SMSLimitCheckService selfToUseCache;

    private static final Logger LOGGER = LoggerFactory.getLogger(SMSLimitCheckService.class);

    private static final int SEND_TIMES = 3;

    /**
     * 短信发送次数限制
     * @param  phone 手机号
     * @return true/false
     */
    public boolean couldSendSMS(String phone) {
        if (selfToUseCache.getSendTimes(phone) < SEND_TIMES) {
            int times = selfToUseCache.addSendTimes(phone);
            LOGGER.debug("Already pass SMS limit check {} times ({}).", times, phone);
            return true;
        }
        return false;
    }

    @CachePut
    @SuppressWarnings("WeakerAccess")
    public int addSendTimes(String phone) {
        LOGGER.debug("Add send times for {}", phone);
        return selfToUseCache.getSendTimes(phone) + 1;
    }

    @Cacheable
    @SuppressWarnings("WeakerAccess")
    public int getSendTimes(String phone) {
        LOGGER.debug("Get send times for {}", phone);
        return 0;
    }
}
