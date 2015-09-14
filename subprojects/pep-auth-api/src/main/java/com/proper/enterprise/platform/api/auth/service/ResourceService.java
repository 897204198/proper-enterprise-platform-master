package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.Resource;

import java.util.Set;

public interface ResourceService {

    /**
     * Get all authorized resources of current user
     *
     * @return resource set
     */
    Set<Resource> getAllResourcesOfCurrentUser();

}
