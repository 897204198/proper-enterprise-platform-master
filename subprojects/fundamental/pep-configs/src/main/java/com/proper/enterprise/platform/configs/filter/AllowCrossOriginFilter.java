package com.proper.enterprise.platform.configs.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 为响应增加允许跨域访问头的过滤器
 */
@Service
@WebFilter(urlPatterns = "/*")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AllowCrossOriginFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllowCrossOriginFilter.class);

    private String allowCredentials;
    private String allowHeaders;
    private String allowMethods;
    private String allowOrigin;
    private String exposeHeaders;
    private String maxAge;

    public AllowCrossOriginFilter(String allowCredentials, String allowHeaders, String allowMethods, String allowOrigin,
                                  String exposeHeaders, String maxAge) {
        this.allowCredentials = allowCredentials;
        this.allowHeaders = allowHeaders;
        this.allowMethods = allowMethods;
        this.allowOrigin = allowOrigin;
        this.exposeHeaders = exposeHeaders;
        this.maxAge = maxAge;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        setAllowCrossOriginHeaders(res);
        if (HttpMethod.OPTIONS.matches(req.getMethod())) {
            LOGGER.trace("Received OPTIONS request {} from {}", req.getRequestURI(), req.getRemoteAddr());
            res.setStatus(HttpStatus.OK.value());
        } else {
            chain.doFilter(request, response);
        }
    }

    private void setAllowCrossOriginHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Credentials", allowCredentials);
        response.setHeader("Access-Control-Allow-Headers", allowHeaders);
        response.setHeader("Access-Control-Allow-Methods", allowMethods);
        response.setHeader("Access-Control-Allow-Origin", allowOrigin);
        response.setHeader("Access-Control-Expose-Headers", exposeHeaders);
        response.setHeader("Access-Control-Max-Age", maxAge);
    }

    @Override
    public void destroy() {
    }

}
