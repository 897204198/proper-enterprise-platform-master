package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.Resource;

import java.util.Set;

public interface ResourceService {
    
    Set<Resource> getResourcesByUser(String userId);

    Set<Resource> getAllResources();

}
