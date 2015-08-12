package com.proper.enterprise.platform.api.auth.service;

import java.util.Set;

import com.proper.enterprise.platform.api.auth.Resource;

public interface ResourceService {
    
    Set<Resource> getResourcesByUser(String userId);

}
