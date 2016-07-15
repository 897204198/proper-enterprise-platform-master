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

import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(path = "/{resourceId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> get(@PathVariable String resourceId) {
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<Resource>> retrieve(
            @RequestParam(name = "type", required = false) ResourceType type) throws Exception {
        boolean isSuper = userService.getCurrentUser().isSuperuser();
        Collection<Resource> resources;

        if (isSuper) {
            resources = type != null ? resourceService.find(type) : resourceService.find();
        } else {
            resources = type != null ? userService.getResources(type) : userService.getResources();
        }

        if (resources.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resources, HttpStatus.OK);
        }
    }

    @RequestMapping(path = "/{resourceId}", method = RequestMethod.PUT)
    public ResponseEntity<Resource> update(@PathVariable String resourceId, @RequestBody ResourceEntity resourceEntity) {
        Resource resource = resourceService.get(resourceId);
        if (resource == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(resourceService.save(resourceEntity), HttpStatus.OK);
    }

    @RequestMapping(path = "/{resourceId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String resourceId, HttpServletResponse response) {
        Resource resource = resourceService.get(resourceId);
        if (resource == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            resourceService.delete(resource);
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }

}
