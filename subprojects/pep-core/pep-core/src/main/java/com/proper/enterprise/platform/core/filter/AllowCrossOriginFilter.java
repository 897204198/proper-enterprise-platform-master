package com.proper.enterprise.platform.core.filter;

import com.proper.enterprise.platform.core.utils.ConfCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 为响应增加允许跨域访问头的过滤器
 */
public class AllowCrossOriginFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllowCrossOriginFilter.class);

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
        response.setHeader("Access-Control-Allow-Credentials", ConfCenter.get("core.access_control.allow_credentials"));
        response.setHeader("Access-Control-Allow-Headers", ConfCenter.get("core.access_control.allow_headers"));
        response.setHeader("Access-Control-Allow-Methods", ConfCenter.get("core.access_control.allow_methods"));
        response.setHeader("Access-Control-Allow-Origin", ConfCenter.get("core.access_control.allow_origin"));
        response.setHeader("Access-Control-Expose-Headers", ConfCenter.get("core.access_control.expose_headers"));
        response.setHeader("Access-Control-Max-Age", ConfCenter.get("core.access_control.max_age"));
    }

    @Override
    public void destroy() {
    }

}
