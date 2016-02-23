package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.auth.common.service.impl.AbstractUserServiceImpl;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.service.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Primary
@Service
public class UserServiceImpl extends AbstractUserServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private JWTService jwtService;

    public User getCurrentUser() {
        HttpServletRequest req =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        LOGGER.debug("Get request from request context holder: {}", req);
        String token = jwtService.getTokenFromHeader(req);
        LOGGER.debug("Get token from request: {}", token);
        JWTHeader header = jwtService.getHeader(token);
        if (header == null) {
            LOGGER.debug("JWT Header is NULL!");
            return null;
        }
        String username = header.getName();
        return getUser(username);
    }

}
