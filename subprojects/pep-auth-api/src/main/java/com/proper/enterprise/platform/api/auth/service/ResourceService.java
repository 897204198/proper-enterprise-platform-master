package com.proper.enterprise.platform.api.auth.service;

import java.util.List;

import com.proper.enterprise.platform.api.auth.Resource;

public interface ResourceService {
    
    List<Resource> getResourcesByUser(String userId);

}
