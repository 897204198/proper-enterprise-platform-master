package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.model.Resource;

import java.util.Collection;

public interface ResourceDao {

    Resource save(Resource resource);

    Collection<? extends Resource> save(Collection<? extends Resource> resources);

    Resource getNewResourceEntity();

    Resource get(String id);

    Collection<? extends Resource> findAll();

    Collection<? extends Resource> findAll(Collection<String> ids);

    void deleteAll();
}
