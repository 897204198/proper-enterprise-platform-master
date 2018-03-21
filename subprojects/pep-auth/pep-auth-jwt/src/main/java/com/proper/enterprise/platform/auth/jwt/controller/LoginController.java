package com.proper.enterprise.platform.auth.jwt.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.AuthcService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.service.JWTAuthcService;
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
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthcService authcService;

    @Autowired
    private JWTAuthcService jwtAuthcService;

    @Autowired
    private UserService userService;

    @AuthcIgnore
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginMap) throws IOException {
        String username = authcService.getUsername(loginMap);
        String pwd = authcService.getPassword(loginMap);

        LOGGER.debug("User {} want to login", username);

        if (authcService.authenticate(username, pwd)) {
            return new ResponseEntity<>(jwtAuthcService.getUserToken(username), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to authenticate", HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/auth/login/user", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getCurrentUser() throws IOException {
        Map<String, String> currentUserMap = new HashMap<>();
        User user = userService.getCurrentUser();
        currentUserMap.put("name", user.getName());
        currentUserMap.put("avatar", "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png");
        currentUserMap.put("userId", user.getId());
        currentUserMap.put("notifyCount", "12");
        return new ResponseEntity<>(currentUserMap, HttpStatus.OK);
    }
}
