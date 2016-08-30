package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.auth.common.service.impl.CommonUserServiceImpl;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.service.JWTService;
import com.proper.enterprise.platform.core.utils.RequestUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class UserServiceImpl extends CommonUserServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private JWTService jwtService;

    public User getCurrentUser() throws IOException {
        HttpServletRequest req = RequestUtil.getCurrentRequest();
        LOGGER.debug("Get request from request context holder: {}", req);
        String token = jwtService.getTokenFromHeader(req);
        LOGGER.debug("Get token from request: {}", token);
        if (StringUtil.isNull(token)) {
            LOGGER.error("Could NOT get token from request!");
            return null;
        }
        JWTHeader header = jwtService.getHeader(token);
        if (header == null) {
            LOGGER.error("JWT Header is NULL!");
            return null;
        }
        String username = header.getName();
        return getByUsername(username);
    }

}
