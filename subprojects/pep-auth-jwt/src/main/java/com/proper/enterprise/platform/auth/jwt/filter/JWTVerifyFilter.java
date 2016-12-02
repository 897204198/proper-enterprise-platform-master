package com.proper.enterprise.platform.auth.jwt.filter;

import com.proper.enterprise.platform.auth.jwt.annotation.JWTIgnore;
import com.proper.enterprise.platform.auth.jwt.authz.AuthzService;
import com.proper.enterprise.platform.auth.jwt.service.HandlerHolder;
import com.proper.enterprise.platform.auth.jwt.service.JWTService;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter request to verify JWT with it
 */
public class JWTVerifyFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTVerifyFilter.class);

    private JWTService jwtService;

    private AuthzService authzService;

    private HandlerHolder handlerHolder;

    private boolean hasContext;

    public void setJwtService(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    public void setAuthzService(AuthzService authzService) {
        this.authzService = authzService;
    }

    public void setHandlerHolder(HandlerHolder handlerHolder) {
        this.handlerHolder = handlerHolder;
    }

    public void setHasContext(boolean hasContext) {
        this.hasContext = hasContext;
    }

    public void destroy() { }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if (shouldIgnore(req)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtService.getTokenFromHeader(req);
        LOGGER.trace("JSON Web Token: " + token);
        if (StringUtil.isNotNull(token) && jwtService.verify(token)) {
            LOGGER.trace("JWT verfiy succeed, invoke next filter in filter chain.");
            filterChain.doFilter(request, response);
        } else {
            LOGGER.trace("JWT verfiy failed.");
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setHeader("WWW-Authenticate",
                "Bearer realm=\"pep\", "
                    + "error=\"invalid_token\", "
                    + "error_description=\"COULD NOT ACCESS THIS API WITHOUT A VALID TOKEN\"");
        }
    }

    /**
     * 判断 request 请求的资源是否不需要 JWT 权限约束
     * 可以通过如下方式设置资源不需要权限：
     * 1. 在 controller 的方法上设置 @JWTIgnore，表示该方法不需要权限
     * 2. 直接在 controller 上设置 @JWTIgnore，表示该 controller 下的所有方法都不需要权限
     * 3. 在 properties 的 auth.jwt.ignorePatterns 中配置
     *
     * @param  req 请求对象
     * @return true：不需要权限约束；false：需要权限约束
     */
    private boolean shouldIgnore(HttpServletRequest req) {
        boolean ignore = false;
        try {
            HandlerMethod handler = handlerHolder.getHandler(req);
            ignore = hasIgnoreOnMethod(handler) || hasIgnoreOnType(handler) || inIgnorePatterns(req);
        } catch (Exception e) {
            LOGGER.error("Find controller error!", e.getCause());
        }
        return ignore;
    }

    private boolean hasIgnoreOnMethod(HandlerMethod handler) {
        boolean ignore = handler.hasMethodAnnotation(JWTIgnore.class);
        if (ignore) {
            LOGGER.trace("Not need JWT of this url caused by @JWTIgnore on method '{}'.", handler.getMethod().getName());
        }
        return ignore;
    }

    private boolean hasIgnoreOnType(HandlerMethod handler) {
        boolean ignore = handler.getBeanType().getAnnotation(JWTIgnore.class) != null;
        if (ignore) {
            LOGGER.trace("Not need JWT of this url caused by @JWTIgnore on type '{}'.", handler.getBeanType().getName());
        }
        return ignore;
    }

    private boolean inIgnorePatterns(HttpServletRequest req) {
        boolean ignore = authzService.shouldIgnore(req.getRequestURI(), req.getMethod(), hasContext);
        if (ignore) {
            LOGGER.debug("Not need JWT of this url({}) caused by settings of AuthzService.ignorePatterns.", req.getRequestURI());
        }
        return ignore;
    }

    public void init(FilterConfig arg0) throws ServletException { }

}
