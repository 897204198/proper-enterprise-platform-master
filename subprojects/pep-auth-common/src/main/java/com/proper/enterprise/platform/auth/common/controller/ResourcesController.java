package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.enums.ResourceType;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/auth/resources")
public class ResourcesController {

    @Autowired
    UserService service;

    @RequestMapping
    public Collection<? extends Resource> getResources() throws Exception {
        return service.getResources(ResourceType.MENU);
    }

}
