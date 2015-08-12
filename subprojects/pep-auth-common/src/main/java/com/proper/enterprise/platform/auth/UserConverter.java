package com.proper.enterprise.platform.auth;

import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.auth.entity.UserEntity;

public class UserConverter {
    
    private UserConverter() { }
    
    public static User toUser(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setUsername(entity.getLoginName());
        user.setPassword(entity.getPassword());
        return user;
    }

}
