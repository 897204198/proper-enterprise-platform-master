package com.proper.enterprise.platform.auth.service;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JWTAuthcServiceImpl implements JWTAuthcService {

    private UserService userService;

    private JWTService jwtService;

    @Autowired
    public JWTAuthcServiceImpl(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public String getUserToken(String username) throws IOException {
        User user = userService.getByUsername(username, EnableEnum.ENABLE);
        JWTHeader header = composeJWTHeader(user);
        JWTPayload payload = composeJWTPayload(user);
        // Allow superuser to login on multi endpoint
        if (!user.getSuperuser()) {
            jwtService.clearToken(header);
        }
        return jwtService.generateToken(header, payload);
    }

    @Override
    public JWTHeader composeJWTHeader(User user) {
        JWTHeader header = new JWTHeader();
        header.setId(user.getId());
        header.setName(user.getUsername());
        return header;
    }

    @Override
    public JWTPayload composeJWTPayload(User user) {
        JWTPayloadImpl payload = new JWTPayloadImpl();
        payload.setHasRole(user.getSuperuser() || CollectionUtil.isNotEmpty(user.getRoles()));
        payload.setName(user.getName());
        return payload;
    }

    @Override
    public void clearUserToken(String username) {
        User user = userService.getByUsername(username, EnableEnum.ALL);
        jwtService.clearToken(composeJWTHeader(user));
    }

}
