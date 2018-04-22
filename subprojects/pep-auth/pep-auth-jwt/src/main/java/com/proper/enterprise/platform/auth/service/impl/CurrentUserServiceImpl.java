package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.service.impl.AbstractUserServiceImpl;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.service.JWTService;
import com.proper.enterprise.platform.core.utils.RequestUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CurrentUserServiceImpl extends AbstractUserServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserServiceImpl.class);

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Override
    public User getCurrentUser() {
        HttpServletRequest req;
        try {
            req = RequestUtil.getCurrentRequest();
            String token = jwtService.getToken(req);
            if (StringUtil.isNull(token)) {
                LOGGER.trace("Could NOT get current user from {} caused by NO token.", req.getRequestURI());
                return null;
            }
            LOGGER.trace("Get token '{}' from request '{}'", token, req.getRequestURI());
            JWTHeader header = jwtService.getHeader(token);
            return userService.getByUsername(header.getName());
        } catch (Exception e) {
            LOGGER.trace("Could NOT get current user caused by {}.", e.getMessage());
            return null;
        }
    }
}
