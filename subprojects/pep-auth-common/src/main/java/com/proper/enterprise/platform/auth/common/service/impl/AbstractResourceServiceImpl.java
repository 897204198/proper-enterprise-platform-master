package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public abstract class AbstractResourceServiceImpl implements ResourceService {

    @Autowired
    UserService userService;

    @Override
    public Set<Resource> getAllResourcesOfCurrentUser() {
        User user = userService.getCurrentUser();
        return (Set<Resource>)userService.getResourcesById(user.getId());
    }

}
