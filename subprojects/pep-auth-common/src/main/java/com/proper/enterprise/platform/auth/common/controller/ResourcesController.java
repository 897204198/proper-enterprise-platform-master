package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/resources")
public class ResourcesController extends BaseController {

    @Autowired
    ResourceService resourceService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource> create(@RequestBody ResourceEntity res) {
        return responseOfPost(resourceService.save(res));
    }

    @RequestMapping(path = "/{resourceId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> find(@PathVariable String resourceId) {
        return responseOfGet(resourceService.get(resourceId));
    }

    @RequestMapping(path = "/{resourceId}", method = RequestMethod.PUT)
    public ResponseEntity<Resource> update(@PathVariable String resourceId, @RequestBody ResourceEntity resourceEntity) {
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            resource = resourceService.save(resourceEntity);
        }
        return responseOfPut(resource);
    }

    @RequestMapping(path = "/{resourceId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String resourceId) {
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            resourceService.delete(resource);
        }
        return responseOfDelete(resource != null);
    }

}
