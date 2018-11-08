package com.proper.enterprise.platform.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Request 工具类
 */
public class RequestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private RequestUtil() { }

    private static UrlPathHelper helper;

    /*
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new RequestUtil();
        helper = new UrlPathHelper();
    }

    /**
     * 获得当前 Request
     *
     * @return 当前 Request
     * @throws IllegalStateException 当前线程未绑定 request 时抛出
     */
    public static HttpServletRequest getCurrentRequest() throws IllegalStateException {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest();
    }

    public static String getURIPath(HttpServletRequest request) {
        String result = "";
        try {
            result = new URI(helper.getPathWithinApplication(request)).getPath();
        } catch (URISyntaxException e) {
            LOGGER.error("Parse URI error!", e);
        }
        return result;
    }

}
