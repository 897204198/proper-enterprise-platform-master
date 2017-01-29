package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.auth.common.service.impl.AbstractUserServiceImpl;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.service.JWTService;
import com.proper.enterprise.platform.core.utils.RequestUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl extends AbstractUserServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private JWTService jwtService;

    public User getCurrentUser() {
        HttpServletRequest req;
        try {
            req = RequestUtil.getCurrentRequest();
            String token = jwtService.getTokenFromHeader(req);
            if (StringUtil.isNull(token)) {
                LOGGER.debug("Could NOT get current user from {} caused by NO token.", req.getRequestURI());
                return null;
            }
            LOGGER.debug("Get token '{}' from request '{}'", token, req.getRequestURI());
            JWTHeader header = jwtService.getHeader(token);
            return getByUsername(header.getName());
        } catch (Exception e) {
            LOGGER.debug("Could NOT get current user caused by {}.", e.getMessage());
            return null;
        }
    }

}
