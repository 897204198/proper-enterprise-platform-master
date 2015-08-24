package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.User;

import java.util.Collection;

public interface UserService {
    
    User getUserByUsername(String username);

    Collection<Resource> getUserResources(String userId);

    Collection<Resource> getUserResourcesByUsername(String username);

    User getCurrentUser();

}
