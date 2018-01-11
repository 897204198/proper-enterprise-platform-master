package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Resource;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

public interface ResourceService {

    Resource save(Resource resource);

    Resource get(String id);

    Resource get(String url, RequestMethod method);

    Collection<Resource> find();

    void delete(Resource resource);

}
