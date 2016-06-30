package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.enums.ResourceType;
import com.proper.enterprise.platform.api.auth.model.Resource;

import java.util.Collection;

public interface ResourceService {

    Resource save(Resource resource);

    Collection<Resource> list();

    Collection<Resource> findByType(ResourceType type);

}
