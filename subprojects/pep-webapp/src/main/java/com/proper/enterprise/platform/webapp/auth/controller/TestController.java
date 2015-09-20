package com.proper.enterprise.platform.webapp.auth.controller;

import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    UserService service;

    @RequestMapping("/index")
    public String test() {
        User user = service.getCurrentUser();
        LOGGER.debug("current user is {}, {}, {}", user.getId(), user.getUsername(), user.getPassword());
        return "auth/common/test";
    }

}
