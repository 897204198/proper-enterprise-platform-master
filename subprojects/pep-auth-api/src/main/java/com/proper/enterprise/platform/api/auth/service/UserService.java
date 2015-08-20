package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.User;

import java.util.Set;

public interface UserService {
    
    User getUserByUsername(String username);

    Set<Resource> getUserResources(String username);

}
