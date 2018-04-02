package com.proper.enterprise.platform.auth.common.filter;

import com.proper.enterprise.platform.core.utils.ConfCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 为响应增加允许跨域访问头的过滤器
 *
 * 此过滤器按设计初衷应该定义在本模块的 web-fragment.xml 中，但会引起 web 应用使用 gretty 插件发布时的无端异常
 * 故采用 servlet 3.0 的注解方式定义
 */
@WebFilter(filterName = "allowCrossOriginFilter", urlPatterns = "/*")
public class AllowCrossOriginFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllowCrossOriginFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        // 此处不能将 if..else 合并，否则同样会引起使用 gretty 插件发布时的问题（OPTIONS 请求直接返回 403 状态码）
        if (HttpMethod.OPTIONS.matches(req.getMethod())) {
            LOGGER.trace("Received OPTIONS request {} from {}", req.getRequestURI(), req.getRemoteAddr());
            setAllowCrossOriginHeaders(res);
            res.setStatus(HttpStatus.OK.value());
        } else {
            setAllowCrossOriginHeaders(res);
            chain.doFilter(request, response);
        }
    }

    private void setAllowCrossOriginHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", ConfCenter.get("auth.access_control.allow_origin"));
        response.setHeader("Access-Control-Allow-Methods", ConfCenter.get("auth.access_control.allow_methods"));
        response.setHeader("Access-Control-Allow-Headers", ConfCenter.get("auth.access_control.allow_headers"));
        response.setHeader("Access-Control-Max-Age", ConfCenter.get("auth.access_control.max_age"));
        response.setHeader("Access-Control-Allow-Credentials", ConfCenter.get("auth.access_control.allow_credentials"));
    }

    @Override
    public void destroy() { }

}
