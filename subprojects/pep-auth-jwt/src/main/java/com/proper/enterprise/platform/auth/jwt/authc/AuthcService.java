package com.proper.enterprise.platform.auth.jwt.authc;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.PasswordEncryptService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthcService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncryptService pwdService;

    public boolean authenticate(String username, String pwd) {
        User user = userService.getByUsername(username);
        return user != null
                && pwdService.encrypt(pwd).equals(user.getPassword());
    }

}
