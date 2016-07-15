package com.proper.enterprise.platform.auth.jwt.filter;

import com.proper.enterprise.platform.auth.jwt.authz.AuthzService;
import com.proper.enterprise.platform.auth.jwt.service.JWTService;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Filter request to verify JWT with it
 */
public class JWTVerifyFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTVerifyFilter.class);

    private JWTService jwtService;

    private AuthzService authzService;

    public void setJwtService(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    public void setAuthzService(AuthzService authzService) {
        this.authzService = authzService;
    }

    public void destroy() { }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        try {
            if (authzService.shouldIgnore(req.getRequestURI(), req.getMethod(), true)) {
                LOGGER.info("Not need JWT of this url({}) caused by settings in AuthzService.ignorePatterns.", req.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }
        } catch (URISyntaxException e) {
            LOGGER.error("Parse URI error! {}", e);
        }

        String token = jwtService.getTokenFromHeader(req);
        LOGGER.info("JSON Web Token: " + token);
        if (StringUtil.isNotNull(token) && jwtService.verify(token)) {
            LOGGER.debug("JWT verfiy succeed, invoke next filter in filter chain.");
            filterChain.doFilter(request, response);
        } else {
            LOGGER.error("JWT verfiy failed.");
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setHeader("WWW-Authenticate",
                    "Bearer realm=\"pep\", "
                            + "error=\"invalid_token\", "
                            + "error_description=\"COULD NOT ACCESS THIS API WITHOUT A VALID TOKEN\"");
        }
    }

    public void init(FilterConfig arg0) throws ServletException { }

}
