package com.proper.enterprise.platform.auth.jwt.filter;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.auth.service.AuthzService;
import com.proper.enterprise.platform.auth.service.HandlerHolder;
import com.proper.enterprise.platform.auth.service.JWTService;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter request to verify JWT with it
 */
@Service("jwtVerifyFilter")
public class JWTVerifyFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTVerifyFilter.class);

    private JWTService jwtService;

    private AuthzService authzService;

    private HandlerHolder handlerHolder;

    @Autowired
    public JWTVerifyFilter(JWTService jwtService, AuthzService authzService, HandlerHolder handlerHolder) {
        this.jwtService = jwtService;
        this.authzService = authzService;
        this.handlerHolder = handlerHolder;
    }

    @Override
    public void destroy() { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if (shouldIgnore(req)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtService.getToken(req);
        LOGGER.trace("JSON Web Token: " + token);
        if (StringUtil.isNotNull(token) && jwtService.verify(token)) {
            LOGGER.trace("JWT verfiy succeed.");
            String userId = jwtService.getHeader(token).getId();
            if (authzService.accessible(req.getRequestURI(), req.getMethod(), userId)) {
                LOGGER.trace("Current user with {} could access {}:{}, invoke next filter in filter chain.",
                    token, req.getMethod(), req.getRequestURI());
                filterChain.doFilter(request, response);
            } else {
                LOGGER.trace("Current user with {} could NOT access {}:{}!", token, req.getMethod(), req.getRequestURI());
                HttpServletResponse resp = (HttpServletResponse) response;
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.setHeader("WWW-Authenticate",
                        "Bearer realm=\"pep\", "
                                + "error=\"invalid_auth\", "
                                + "error_description=\"COULD NOT ACCESS THIS API WITHOUT A PROPER AUTHORIZATION\"");
            }
        } else {
            LOGGER.trace("JWT verfiy failed.");
            HttpServletResponse resp1 = (HttpServletResponse) response;
            resp1.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp1.setHeader("WWW-Authenticate",
                    "Bearer realm=\"pep\", "
                            + "error=\"invalid_auth\", "
                            + "error_description=\"COULD NOT ACCESS THIS API WITHOUT A PROPER AUTHENTICATION\"");
        }
    }

    /**
     * 判断 request 请求的资源是否不需要 JWT 权限约束
     * 可以通过如下方式设置资源不需要权限：
     * 1. 在 controller 的方法上设置 @AuthcIgnore，表示该方法不需要权限
     * 2. 直接在 controller 上设置 @AuthcIgnore，表示该 controller 下的所有方法都不需要权限
     * 3. 在 properties 的 auth.jwt.ignorePatterns 中配置
     *
     * @param  req 请求对象
     * @return true：不需要权限约束；false：需要权限约束
     */
    private boolean shouldIgnore(HttpServletRequest req) {
        boolean ignore = inIgnorePatterns(req);
        if (ignore) {
            return true;
        }
        try {
            HandlerMethod handler = handlerHolder.getHandler(req);
            ignore = hasIgnoreOnMethod(handler) || hasIgnoreOnType(handler);
        } catch (Exception e) {
            LOGGER.debug("Could NOT find controller for {}!", req.getRequestURI());
        }
        return ignore;
    }

    private boolean hasIgnoreOnMethod(HandlerMethod handler) {
        boolean ignore = handler.hasMethodAnnotation(AuthcIgnore.class);
        if (ignore) {
            LOGGER.trace("Not need JWT of this url caused by @AuthcIgnore on method '{}'.", handler.getMethod().getName());
        }
        return ignore;
    }

    private boolean hasIgnoreOnType(HandlerMethod handler) {
        boolean ignore = handler.getBeanType().getAnnotation(AuthcIgnore.class) != null;
        if (ignore) {
            LOGGER.trace("Not need JWT of this url caused by @AuthcIgnore on type '{}'.", handler.getBeanType().getName());
        }
        return ignore;
    }

    private boolean inIgnorePatterns(HttpServletRequest req) {
        boolean ignore = authzService.shouldIgnore(req.getRequestURI(), req.getMethod());
        if (ignore) {
            LOGGER.debug("Not need JWT of this url({}) caused by settings of AuthzService.ignorePatterns.", req.getRequestURI());
        }
        return ignore;
    }

    @Override
    public void init(FilterConfig arg0) { }

}
