package com.proper.enterprise.platform.auth.jwt.authc;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl;
import com.proper.enterprise.platform.auth.jwt.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JWTAuthcService {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    public String getUserToken(String username) throws IOException {
        User user = userService.getByUsername(username);
        JWTHeader header = new JWTHeader();
        header.setId(user.getId());
        header.setName(user.getUsername());
        JWTPayloadImpl payload = new JWTPayloadImpl();
        jwtService.clearToken(header);
        return jwtService.generateToken(header, payload);
    }

}
