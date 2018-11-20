package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.AuthcService;
import com.proper.enterprise.platform.api.auth.service.PasswordEncryptService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthcServiceImpl implements AuthcService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncryptService pwdService;

    @Override
    public boolean authenticate(String username, String pwd) {
        User user = userService.getByUsername(username, EnableEnum.ENABLE);
        return user != null
            && pwdService.encrypt(pwd).equals(user.getPassword());
    }

}
