package com.proper.enterprise.platform.auth.jwt.controller;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.jwt.authc.AuthcService;
import com.proper.enterprise.platform.auth.jwt.bean.LoginBean;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl;
import com.proper.enterprise.platform.auth.jwt.service.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired private AuthcService authcService;
    @Autowired private JWTService jwtService;
    @Autowired private UserService userService;

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginBean loginBean) throws IOException {
        String username = loginBean.getUsername();
        String pwd = loginBean.getPwd();

        LOGGER.debug("User {} want to login", username);

        if (authcService.authenticate(username, pwd)) {
            User user = userService.getByUsername(username);
            JWTHeader header = new JWTHeader();
            header.setId(user.getId());
            header.setName(user.getUsername());
            JWTPayloadImpl payload = new JWTPayloadImpl();
            jwtService.clearToken(header);
            return new ResponseEntity<String>(jwtService.generateToken(header, payload), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Failed to authenticate", HttpStatus.UNAUTHORIZED);
        }
    }

}
