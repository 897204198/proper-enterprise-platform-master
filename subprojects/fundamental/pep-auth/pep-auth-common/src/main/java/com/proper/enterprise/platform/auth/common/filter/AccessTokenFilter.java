package com.proper.enterprise.platform.auth.common.filter;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.api.auth.service.AuthzService;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Filter request to verify access token
 */
@Service
@WebFilter(urlPatterns = "/*")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccessTokenFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenFilter.class);

    private AccessTokenService accessTokenService;

    private AuthzService authzService;

    private HandlerHolder handlerHolder;

    @Autowired
    public AccessTokenFilter(AccessTokenService accessTokenService, AuthzService authzService,
                             HandlerHolder handlerHolder) {
        this.accessTokenService = accessTokenService;
        this.authzService = authzService;
        this.handlerHolder = handlerHolder;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if (shouldIgnore(req)) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> token = accessTokenService.getToken(req);
        if (token.isPresent() && accessTokenService.verify(token.get())) {
            LOGGER.trace("Access token verify succeed.");
            Optional<String> userId = accessTokenService.getUserId(token.get());
            if (userId.isPresent()) {
                Authentication.setCurrentUserId(userId.get());
                if (authzService.accessible(req, userId.get())) {
                    LOGGER.trace("Current user with {} could access {}:{}, invoke next filter in filter chain.",
                        token.get(), req.getMethod(), req.getRequestURI());
                    filterChain.doFilter(request, response);
                } else {
                    LOGGER.trace("Current user with {} could NOT access {}:{}!", token.get(), req.getMethod(), req.getRequestURI());
                    HttpServletResponse resp = (HttpServletResponse) response;
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.setHeader("WWW-Authenticate",
                        "Bearer realm=\"pep\", "
                            + "error=\"invalid_auth\", "
                            + "error_description=\"COULD NOT ACCESS THIS API WITHOUT A PROPER AUTHORIZATION\"");
                }
            }
        } else {
            LOGGER.trace("Access token verfiy failed.");
            HttpServletResponse resp1 = (HttpServletResponse) response;
            resp1.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp1.setHeader("WWW-Authenticate",
                "Bearer realm=\"pep\", "
                    + "error=\"invalid_auth\", "
                    + "error_description=\"COULD NOT ACCESS THIS API WITHOUT A PROPER AUTHENTICATION\"");
        }
    }

    /**
     * 判断 request 请求的资源是否不需要权限约束
     * 可以通过如下方式设置资源不需要权限：
     * 1. 在 controller 的方法上设置 @AuthcIgnore，表示该方法不需要权限
     * 2. 直接在 controller 上设置 @AuthcIgnore，表示该 controller 下的所有方法都不需要权限
     * 3. 按照 ignorePatternsList 中的模式，在 list bean 中添加忽略配置
     *
     * @param req 请求对象
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
            LOGGER.debug("Could NOT find controller for {}, it may just no need one or wrong access, maybe not an error.",
                RequestUtil.getURIPath(req));
        }
        return ignore;
    }

    private boolean hasIgnoreOnMethod(HandlerMethod handler) {
        boolean ignore = handler.hasMethodAnnotation(AuthcIgnore.class);
        if (ignore) {
            LOGGER.trace("Not need token of this url caused by @AuthcIgnore on method '{}'.", handler.getMethod().getName());
        }
        return ignore;
    }

    private boolean hasIgnoreOnType(HandlerMethod handler) {
        boolean ignore = handler.getBeanType().getAnnotation(AuthcIgnore.class) != null;
        if (ignore) {
            LOGGER.trace("Not need token of this url caused by @AuthcIgnore on type '{}'.", handler.getBeanType().getName());
        }
        return ignore;
    }

    private boolean inIgnorePatterns(HttpServletRequest req) {
        boolean ignore = authzService.shouldIgnore(req);
        if (ignore) {
            LOGGER.debug("Not need token of this url({}) caused by settings of AuthzService.ignorePatterns.", req.getRequestURI());
        }
        return ignore;
    }

    @Override
    public void init(FilterConfig arg0) {
    }

}
