package com.proper.enterprise.platform.api.auth.service;

import java.util.Collection;

import org.springframework.web.bind.annotation.RequestMethod;

import com.proper.enterprise.platform.api.auth.enums.ResourceType;
import com.proper.enterprise.platform.api.auth.model.Resource;

public interface ResourceService {

    Resource save(Resource resource);

    Resource get(String id);

    void delete(Resource resource);

    Collection<Resource> find();

    Collection<Resource> find(ResourceType type);
    
    Resource get(String url, RequestMethod method);


}
