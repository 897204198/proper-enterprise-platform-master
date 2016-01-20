package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.core.enums.ResourceType;

import java.util.Collection;

public interface UserService {
    
    User getUserByUsername(String username);

    Collection<Resource> getUserResources(String userId);

    Collection<Resource> getUserResourcesByUsername(String username);

    User getCurrentUser();

    void addUser(User... users);

    /**
     * Get all authorized resources of current user
     *
     * @return resources collection
     */
    Collection<Resource> getResources();

    /**
     * Get all authorized resources of current user by ResourceType
     * @param resourceType
     * @return
     */
    Collection<Resource> getResources(ResourceType resourceType);

}
