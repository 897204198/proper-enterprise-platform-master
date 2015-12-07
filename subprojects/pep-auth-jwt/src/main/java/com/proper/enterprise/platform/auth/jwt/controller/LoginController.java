package com.proper.enterprise.platform.auth.jwt.controller;

import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.jwt.authc.AuthcService;
import com.proper.enterprise.platform.auth.jwt.bean.LoginBean;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl;
import com.proper.enterprise.platform.auth.jwt.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired private AuthcService authcService;
    @Autowired private JWTService jwtService;
    @Autowired private UserService userService;

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(LoginBean loginBean) {
        String username = loginBean.getUsername();
        String pwd = loginBean.getPwd();

        if (authcService.authenticate(username, pwd)) {
            User user = userService.getUserByUsername(username);
            JWTHeader header = new JWTHeader();
            header.setId(user.getId());
            header.setName(user.getUsername());
            JWTPayloadImpl payload = new JWTPayloadImpl();
            return new ResponseEntity<String>(jwtService.generateToken(header, payload), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Failed to authenticate", HttpStatus.UNAUTHORIZED);
        }
    }
}
