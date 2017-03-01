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
        JWTHeader header = new JWTHeader();
        header.setId(user.getId());
        header.setName(user.getUsername());
        JWTPayloadImpl payload = new JWTPayloadImpl();
        payload.setHasRole(user.isSuperuser() || CollectionUtil.isNotEmpty(user.getRoles()));
        jwtService.clearToken(header);
        return jwtService.generateToken(header, payload);
    }

}
