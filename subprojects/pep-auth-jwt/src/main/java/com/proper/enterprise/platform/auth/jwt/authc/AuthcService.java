package com.proper.enterprise.platform.auth.jwt.authc;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthcService {

    @Autowired
    private UserService userService;

    public boolean authenticate(String username, String pwd) {
        User user = userService.getUser(username);
        return username.equals(user.getUsername())
                && MD5Util.md5Hex(pwd).equals(user.getPassword());
    }

}
