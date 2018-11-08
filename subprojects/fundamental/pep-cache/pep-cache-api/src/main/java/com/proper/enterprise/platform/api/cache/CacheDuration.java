package com.proper.enterprise.platform.api.cache;

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
     * The maximum number of milliseconds an element can exist in the cache regardless of use.
     * The element expires at this limit and will no longer be returned from the cache.
     * The default value is 0, which means no timeToLive (TTL) eviction takes place (infinite lifetime).
     *
     * @return time to live
     */
    long ttl() default 0;

    /**
     * The maximum number of milliseconds an element can exist in the cache without being accessed.
     * The element expires at this limit and will no longer be returned from the cache.
     * The default value is 0, which means no timeToIdle (TTI) eviction takes place (infinite lifetime).
     *
     * @return max idle time
     */
    long maxIdleTime() default 0;

}
