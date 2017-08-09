package com.proper.enterprise.platform.auth.jwt.service;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JWTAuthcServiceImpl implements JWTAuthcService {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Override
    public String getUserToken(String username) throws IOException {
        User user = userService.getByUsername(username);
        JWTHeader header = composeJWTHeader(user.getId(), user.getUsername());
        JWTPayloadImpl payload = new JWTPayloadImpl();
        payload.setHasRole(user.isSuperuser() || CollectionUtil.isNotEmpty(user.getRoles()));
        jwtService.clearToken(header);
        return jwtService.generateToken(header, payload);
    }

    private JWTHeader composeJWTHeader(String userId, String username) {
        JWTHeader header = new JWTHeader();
        header.setId(userId);
        header.setName(username);
        return header;
    }

    @Override
    public void clearUserToken(String username) {
        User user = userService.getByUsername(username);
        jwtService.clearToken(composeJWTHeader(user.getId(), user.getUsername()));
    }

}
