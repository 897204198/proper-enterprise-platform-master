package com.proper.enterprise.platform.core.security.service.impl;

import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.service.JWTService;
import com.proper.enterprise.platform.core.security.service.SecurityService;
import com.proper.enterprise.platform.core.utils.RequestUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class JWTSecurityServiceImpl implements SecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTSecurityServiceImpl.class);

    @Autowired
    private JWTService jwtService;

    @Override
    public String getCurrentUserId() {
        JWTHeader jwtHeader = getJWTHeader();
        if (null == jwtHeader) {
            return null;
        }
        return getJWTHeader().getId();
    }

    private JWTHeader getJWTHeader() {
        HttpServletRequest req;
        try {
            req = RequestUtil.getCurrentRequest();
            String token = jwtService.getToken(req);
            if (StringUtil.isNull(token)) {
                LOGGER.trace("Could NOT get current user from {} caused by NO token.", req.getRequestURI());
                return null;
            }
            LOGGER.trace("Get token '{}' from request '{}'", token, req.getRequestURI());
            return jwtService.getHeader(token);
        } catch (Exception e) {
            LOGGER.trace("Could NOT get current user caused by {}.", e.getMessage());
            return null;
        }
    }
}
