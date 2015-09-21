package com.proper.enterprise.platform.webapp.auth.controller;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/auth/resources")
public class ResourcesController {

    @Autowired
    ResourceService service;

    @RequestMapping
    public Set<Resource> getResources() {
        return service.getAllResourcesOfCurrentUser();
    }

}
