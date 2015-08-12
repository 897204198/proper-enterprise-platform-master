package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.User;

public interface UserService {
    
    User getUserByUsername(String username);

}
