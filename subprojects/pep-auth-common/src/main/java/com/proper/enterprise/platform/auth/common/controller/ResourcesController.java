package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/auth/resources")
public class ResourcesController {

    @Autowired
    UserService userService;

    @Autowired
    ResourceService resourceService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Resource create(@RequestBody ResourceEntity res) {
        return resourceService.save(res);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<? extends Resource>> retrieve() throws Exception {
//        return userService.getResources(ResourceType.MENU);
        return null;
    }

}
