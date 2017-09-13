package com.proper.enterprise.platform.cache;

import java.lang.annotation.*;

/**
 * Dynamic define ttl and max idle time with cache name.
 * MUST use with spring cache annotations TOGETHER.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CacheDuration {

    /**
     * Cache name.
     * Defaults to full name of the method or type using this annotation,
     * such as: com.proper.enterprise.platform.cache.redis.service.CacheableService#testCacheDuration
     *
     * @return cache name
     */
    String cacheName() default "";

    /**
     * Time to live in milliseconds.
     * 0 means infinite.
     *
     * @return time to live
     */
    long ttl() default 0;

    /**
     * Max idle time in milliseconds.
     * 0 means infinite.
     *
     * @return max idle time
     */
    long maxIdleTime() default 0;

}
