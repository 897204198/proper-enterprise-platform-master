package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    UserService userService;

    @Override
    public Set<Resource> getAllResourcesOfCurrentUser() {
        User user = userService.getCurrentUser();
        return (Set<Resource>)userService.getUserResources(user.getId());
    }

}
