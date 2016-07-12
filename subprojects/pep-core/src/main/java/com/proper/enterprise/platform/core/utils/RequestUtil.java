package com.proper.enterprise.platform.core.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Request 工具类
 */
public class RequestUtil {

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private RequestUtil() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new RequestUtil();
    }

    /**
     * 获得当前 Request
     *
     * @return 当前 Request
     */
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest();
    }

}
