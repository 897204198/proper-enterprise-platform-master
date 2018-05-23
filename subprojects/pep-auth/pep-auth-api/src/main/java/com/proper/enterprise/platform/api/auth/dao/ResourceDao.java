package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

public interface ResourceDao extends BaseService<Resource, String> {

    Resource save(Resource resource);

    Resource updateForSelective(Resource resource);

    Collection<? extends Resource> findAll(EnableEnum enableEnum);

    Collection<? extends Resource> findAll(String name, EnableEnum enableEnum);

    Collection<? extends Resource> findAll(Collection<String> ids);

    Resource getNewResourceEntity();

    Resource get(String id);

    void deleteAll();
}
