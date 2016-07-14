package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.enums.ResourceType;
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
    public ResponseEntity<Collection<Resource>> retrieve(
            @RequestParam(name = "type", required = false) ResourceType type) throws Exception {
        boolean isSuper = userService.getCurrentUser().isSuperuser();
        Collection<Resource> resources;

        if (isSuper) {
            resources = type != null ? resourceService.findByType(type) : resourceService.list();
        } else {
            resources = type != null ? userService.getResources(type) : userService.getResources();
        }

        if (resources.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resources, HttpStatus.OK);
        }
    }

}
