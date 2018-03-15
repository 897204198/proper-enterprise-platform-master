package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

public interface ResourceDao extends BaseService<Resource, String> {

    Resource save(Resource resource);

    Resource getNewResourceEntity();

    Resource get(String id);

    Collection<? extends Resource> findAll(Collection<String> ids);

    void deleteAll();
}
